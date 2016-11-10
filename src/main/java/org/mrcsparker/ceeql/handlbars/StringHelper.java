package org.mrcsparker.ceeql.handlbars;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public enum StringHelper implements Helper<Object> {
	
	concat {
		@Override
		public CharSequence apply(final Object context, final Options options) {
			if (options.isFalsy(context)) {
				return "";
			}
			return safeApply(context, options);
		}

		@SuppressWarnings("rawtypes")
		protected CharSequence safeApply(final Object context, final Options options) {
			String separator = options.hash("separator", "");
			StringBuilder sb = new StringBuilder();
			
			sb.append(join(context, separator));
			
			for(int i=0; options.param(i, null) != null; i++) {
				sb.append(join(options.param(i), separator));
        	}

			return sb.toString();
		}
	};
	StringHelper(){}
	StringHelper(String... names){this.names = names;}
	
	private String[] names;
	
	private String[] aliases() {
		return (names==null)? new String[]{name()} : names;
	}

    public StringHelper registerHelper(final Handlebars handlebars) {
    	for (String name: aliases())
    		handlebars.registerHelper(name, this);
    	return this;
    }

    public static void register(final Handlebars handlebars) {
    	StringHelper[] helpers = values();
        for (StringHelper helper : helpers) {
          helper.registerHelper(handlebars);
        }
	}

    //TODO: dry
	private static String join(Object o, String separator) {
		String s = "";
		if (o == null) return s;
		if (o instanceof Iterable) {
   			s = StringUtils.join((Iterable) o, ",");
		} else if (o instanceof Iterator) {
			s = StringUtils.join((Iterator) o, ",");
		} else if (o.getClass().isArray()) {
			s = StringUtils.join((Object[]) o, ",");
		} else {   	
			s = o.toString();
		}
		return s;
	}

}
