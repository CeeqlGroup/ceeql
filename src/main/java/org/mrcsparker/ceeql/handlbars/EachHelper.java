package org.mrcsparker.ceeql.handlbars;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter;
import org.mrcsparker.ceeql.jdbi.NamedParameterRewriter.NameList;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.PreparedBatch;
import org.skife.jdbi.v2.PreparedBatchPart;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;


/**
 * Override default helper to include batch and named parameters
 */
public class EachHelper implements Helper<Object> {
	
  /**
   * The helper's name.
   */
  public static final String NAME = "each";

  private Map<String, String> parameters;
  private Handle dbiHandle;
  private NameList names;
  public PreparedBatch batch; 
  public boolean isBatch;
  
  public EachHelper(Map<String, String> parameters, NameList names, boolean isBatch, Handle dbiHandle) {
	this.parameters = parameters;
	this.names = names;
	this.dbiHandle = dbiHandle;
	this.isBatch = isBatch;
  }

  @SuppressWarnings({"rawtypes", "unchecked" })
  @Override
  public CharSequence apply(final Object context, final Options options)
      throws IOException {
    if (context instanceof Iterable) {
      Options.Buffer buffer = options.buffer();
      Iterator<Object> loop = ((Iterable) context).iterator();
      int base = options.hash("base", 0);
      int index = base;
      boolean even = index % 2 == 0;
      Context parent = options.context;
      Template fn = options.fn;
      while (loop.hasNext()) {
        Object it = loop.next();
        Context itCtx = Context.newContext(parent, it);
        itCtx.combine("@index", index)
            .combine("@first", index == base ? "first" : "")
            .combine("@last", !loop.hasNext() ? "last" : "")
            .combine("@odd", even ? "" : "odd")
            .combine("@even", even ? "even" : "")
            // 1-based index
            .combine("@index_1", index + 1);
        CharSequence cs = options.apply(fn, itCtx, Arrays.asList(it, index));
        NamedParameterRewriter.ParsedStatement ps = NamedParameterRewriter.parseString(cs.toString(), parameters, names, itCtx);
        if (isBatch) {
	        if (index == base) {
	        	buffer.append(ps.getParsedSql());
	        	batch = dbiHandle.prepareBatch(ps.getParsedSql());
	        } 
	        PreparedBatchPart part = batch.add();
	        for( Map.Entry<String, String> e: parameters.entrySet()) {
	        	part.bind(e.getKey(), e.getValue());
	        }
	        parameters.clear();
	    	names.rewind();
        } else {
        	buffer.append(ps.getParsedSql());
        	names.reset();
        }
        index += 1;
        even = !even;
      }
      // empty?
      if (base == index) {
        buffer.append(options.inverse());
      }
      return buffer;
    } else if (context != null) {
      Iterator loop = options.propertySet(context).iterator();
      Context parent = options.context;
      boolean first = true;
      Options.Buffer buffer = options.buffer();
      Template fn = options.fn;
      while (loop.hasNext()) {
        Entry entry = (Entry) loop.next();
        Object key = entry.getKey();
        Object value = entry.getValue();
        Context itCtx = Context.newBuilder(parent, value)
            .combine("@key", key)
            .combine("@first", first ? "first" : "")
            .combine("@last", !loop.hasNext() ? "last" : "")
            .build();
        CharSequence cs = options.apply(fn, itCtx, Arrays.asList(value, key));
        NamedParameterRewriter.ParsedStatement ps = NamedParameterRewriter.parseString(cs.toString(), parameters, names, itCtx);
        if (isBatch) {
	        if (first) {
	        	buffer.append(ps.getParsedSql());
	        	batch = dbiHandle.prepareBatch(ps.getParsedSql());
	        } 
	        PreparedBatchPart part = batch.add();
	        for( Map.Entry<String, String> e: parameters.entrySet()) {
	        	part.bind(e.getKey(), e.getValue());
	        }
	        parameters.clear();
	    	names.rewind();
        } else {
        	buffer.append(ps.getParsedSql());
        	names.reset();
        }
        first = false;
      }
      // empty?
      if (first) {
        buffer.append(options.inverse());
      }
      return buffer;
    }
    return options.buffer();
  }

}