package com.techport80.bzscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techport80.bzscript.Language.Bz;

public class SyntaxRules {
    public static class Rule {
        {@rules;
        public static final Map<Bz.Kind, List<Bz.Kind>> {=ruleTableName:}
            = new HashMap<>();

        static {
            {@ruleTable;
            {
                List<Bz.Kind> ls = new ArrayList<>();
                {@listOfKinds;
                ls.add(Bz.Kind.{=kind:});
                :}
                {=ruleTableName:}.put(Bz.Kind.{=subject:}, ls);
            }
            :}
        }
        :}
    }
}