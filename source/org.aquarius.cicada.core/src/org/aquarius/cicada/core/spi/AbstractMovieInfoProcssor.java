/**
 * 
 */
package org.aquarius.cicada.core.spi;

import org.aquarius.cicada.core.model.Movie;
import org.aquarius.service.INameService;
import org.aquarius.util.base.AbstractComparable;
import org.aquarius.util.mark.IUrlAcceptable;

/**
 * @author aquarius.github@hotmail.com
 *
 */
public abstract class AbstractMovieInfoProcssor extends AbstractComparable<AbstractMovieInfoProcssor>
		implements IUrlAcceptable, INameService<AbstractMovieInfoProcssor> {

	/**
	 * 
	 * @param movie
	 * @return
	 */
	public abstract void process(Movie movie);
}
