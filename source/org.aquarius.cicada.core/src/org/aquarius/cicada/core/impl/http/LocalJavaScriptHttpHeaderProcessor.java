/**
 * 
 */
package org.aquarius.cicada.core.impl.http;

import java.io.File;
import java.net.HttpURLConnection;

import javax.script.Invocable;

import org.apache.commons.lang.StringUtils;
import org.aquarius.cicada.core.script.LocalJavaScriptEngineObject;
import org.aquarius.cicada.core.spi.AbstractHttpHeaderProcessor;
import org.aquarius.log.LogUtil;
import org.aquarius.service.IReloadable;
import org.slf4j.Logger;

/**
 * @author aquarius.github@hotmail.com
 *
 */
public class LocalJavaScriptHttpHeaderProcessor extends AbstractHttpHeaderProcessor implements IReloadable {

	private Logger log = LogUtil.getLogger(getClass());

	private LocalJavaScriptEngineObject localJavaScriptEngineObject;

	/**
	 * 
	 */
	public LocalJavaScriptHttpHeaderProcessor(File file) {
		super();

		this.localJavaScriptEngineObject = new LocalJavaScriptEngineObject(file);
	}

	/**
	 * 
	 */
	public LocalJavaScriptHttpHeaderProcessor(String name, File file) {
		super();

		this.localJavaScriptEngineObject = new LocalJavaScriptEngineObject(name, file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.localJavaScriptEngineObject.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAcceptable(String urlString) {
		return StringUtils.containsIgnoreCase(urlString, this.localJavaScriptEngineObject.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean reload() {
		return this.localJavaScriptEngineObject.reload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(HttpURLConnection httpConnection) {
		try {
			Invocable invocable = (Invocable) this.localJavaScriptEngineObject.getScriptEngine();
			invocable.invokeFunction("doProcess", httpConnection);

		} catch (Exception e) {
			this.log.error(this.localJavaScriptEngineObject.getName() + " process error ", e);
		}
	}

}
