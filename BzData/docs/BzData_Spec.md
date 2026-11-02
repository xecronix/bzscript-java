# BzData Format

BzData is a plain-text format consisting of data records, optional comments, and an optional header. Ordinary comment records are exempt from field-count validation; the header participates in the count.

## Records

- A semicolon (`;`) outside a field terminates a record.
- Every record, including comments and the header, must terminate with a semicolon.
- Operating-system line endings do not terminate records.
- Whitespace before the first field or between records is insignificant.
- Only whitespace is allowed before the first field of a data record, or between the header marker (`!`) and its first field.
- Text after a field belongs to that field as a comment, including its whitespace.
- A record may span multiple physical lines.

## Fields

- Each field begins and ends with a pipe (`|`).
- Whitespace and physical line endings inside fields are preserved.
- A semicolon inside a field is ordinary data.
- An exclamation mark inside a field is ordinary data.
- An empty field is written as `||`.
- Fields require no additional separators.

These records each contain three fields:

```text
|first||second||third|;
|first|    |second|    |third|;
```

## Escaping

Inside a field:

- `\\` represents one literal backslash.
- `\|` represents one literal pipe.

If a backslash is followed by any other character, preserve the backslash unchanged. For example, `\n` and `\s` each remain two ordinary characters.

Scan left to right. Each recognized escape consumes both characters. An unescaped pipe closes the field.

Examples:

```text
|more\| data|;
```

Contains `more| data`.

```text
|C:\somewhere\\|;
```

Contains `C:\somewhere\`.

```text
|\\\||;
```

Contains a backslash followed by a pipe.

## Comments

### Ordinary comment records

- A record beginning with `!`, ignoring insignificant whitespace, is a comment record.
- If that record is the first record in the file, it is the header and follows the header rules instead.
- For an ordinary comment record, the next semicolon ends the record.
- Every character before that semicolon is ordinary comment text, including pipes, backslashes, exclamation marks, and physical line endings.
- Fields and escape sequences are not recognized inside ordinary comment records.
- Ordinary comment records are excluded from field-count validation.
- Comments must terminate with a semicolon.

```text
! this is a comment ;
```

### Field comments

- Each field owns the comment following its closing pipe, up to the next opening pipe or record-terminating semicolon.
- Field comments may span physical lines.
- No comment marker is required.
- All comment whitespace and physical line endings are preserved.
- A pipe starts the next field; a semicolon ends the record. Neither character can be included literally in a field comment.
- Backslashes have no escape behavior in comments.
- An exclamation mark after a field is ordinary comment text; it does not start a separate comment record.
- Comment strings exclude structural delimiters. The pipe closing a field, the pipe opening the next field, and the record-terminating semicolon are not stored in the field comment.
- Before the first field, only whitespace is permitted. There is no preceding field to own commentary in that position.
- Between records, only whitespace or a separate semicolon-terminated comment record may appear.

This data record contains three fields, each followed by its own comment:

```text
|first| First value
|second| Second value
|third| Third value;
```

## Header

- A comment record that is the first record in the file is the header.
- The first record therefore cannot be an ordinary comment record. There is no separate first-record comment marker.
- The header contains column names using the same field layout and escaping rules as data records.
- Comments follow their header fields and are stored with those fields.
- Only whitespace is permitted between the initial `!` and the first header field.
- Decoded column names are trimmed in memory for validation and name-based lookup, using Java's `String.trim()` behavior. Internal whitespace remains significant.
- Every trimmed column name must be nonempty and unique. Empty names (`||`) and names containing only trimmable whitespace are invalid.
- Column names are case-sensitive: `Name` and `name` are distinct names.
- Lookup names are trimmed too, so `Name` and ` Name ` address the same column. Those two names cannot appear as separate columns in one header.
- The original decoded header field data is retained, including its leading and trailing whitespace. Saving preserves that whitespace rather than writing the trimmed lookup name; normal field escaping still applies.
- The header must have the same number of fields as every data record.

```text
!|msg1||msg2||msg3|;
```

A header may also describe its columns across multiple physical lines:

```text
!
    |msg1| First message
    |msg2| Second message
    |msg3| Third message
;
```

## Object Model

### BzData

- Contains a list of `BzRecord` objects.
- Provides record iteration functions.
- Does not require the first column of each record to be unique across the file.

### BzRecord

- Contains a list of `BzField` objects and a record kind.
- Provides field iteration functions.

| Kind | Meaning | Field-count validation |
| --- | --- | --- |
| `HEADER` | The first record of the file, when it begins with `!`; its fields name the columns. | Establishes the required count. |
| `FIRST` | The first record of the file, when it is a data record rather than a header. | Establishes the required count. |
| `DATA` | A subsequent data record. | Must match the established count. |
| `COMMENT` | An ordinary comment record after the first record. | Skipped. |

A file beginning with a header has no `FIRST` record. Its first data record has kind `DATA`.

A `COMMENT` record is expected to contain a single `BzField`. Its data string is empty; its comment string contains everything after the opening `!` and before the terminating semicolon, including all whitespace. These two delimiters are excluded. Pipes and additional exclamation marks within the ordinary comment remain literal text.

### BzField

- Contains a string for data and a string for its following comment.
- Data contains the decoded field value, excluding the opening and closing pipes.
- Comment contains the following comment text with all whitespace preserved, excluding structural delimiters.

### Future keyed container

A possible future `BzKeyedRecord` container would guarantee that its child records have unique first-column values. This constraint is not part of the current format or `BzData` container.

## Empty Containers

- An empty file or a whitespace-only file creates a `BzData` object containing zero records.
- If a file contains records, its first record, whether a header or data record, must contain at least one field. An initial `;` or `!;` is invalid, even if followed by a record containing fields.
- Subsequent data records must match the established field count, so zero-field data records are invalid as well.
- Ordinary `COMMENT` records are retained as described in the object model and are exempt from field-count validation.
- An empty container can serve as a placeholder for future data.
- Empty fields and zero-field records are different: `||;` is a valid data record containing one empty field; `;` contains no fields and is invalid. An empty header field remains invalid.
- Empty and whitespace-only files are exceptions to the final-semicolon requirement. Any record that is present still requires its terminating semicolon.

## Text Encoding

Use the host language's default text-file encoding behavior when no encoding is specified. The format does not currently mandate an encoding or separate BOM behavior.

## Validation

A reader must throw an exception when:

1. Data records do not all contain the same number of fields.
2. A header contains duplicate field names, using case-sensitive comparison of trimmed decoded field values.
3. A data record's field count differs from the header's field count.
4. Any record lacks its terminating semicolon.
5. A file containing records does not end with a record-terminating semicolon followed by zero or more whitespace characters.
6. A field is opened but never closed by an unescaped pipe.
7. A header contains a column name that is empty after trimming.
8. Non-whitespace text appears before the first field of a data record, or between the header marker and its first field.
9. The first record, whether a header or data record, contains no fields.

When a header is present, its field count establishes the required count. Otherwise, the `FIRST` record establishes the required count. Ordinary `COMMENT` records are skipped during field-count validation. Field comments do not contribute to field counts.

A semicolon contained inside a field does not satisfy the record-termination requirement and cannot close an unterminated field.

## Complete Example

```text
!
    |msg1| First message
    |msg2| Second message
    |msg3| Third message
;
|data||more\| data||even ;more ;data|;

! this is a comment ;

|another|    |record|    |with spaces preserved here|;
```

The header and both data records contain three fields. The ordinary comment does not participate in the field count. Each description following a header field is stored as that field's comment, including its whitespace.
