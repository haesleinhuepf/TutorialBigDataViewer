/**
 *
 */
package de.mpicbg.jug.plugins;

import io.scif.services.DatasetIOService;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.scijava.command.Command;
import org.scijava.io.IOService;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvHandlePanel;
import bdv.util.BdvSource;
import net.imagej.Dataset;
import net.imagej.ImgPlus;
import net.imagej.display.DatasetView;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;

/**
 * Plugin that opens the active image using the
 * <code>GenericClearVolumeGui</code>.
 *
 * @author jug
 */
@Plugin( menu = { @Menu( label = "Tutorials" ),
				  @Menu( label = "BDV Vistools" ),
				  @Menu( label = "Tutorial Plugin 3" ) }, description = "Multiple sources, tainted.", headless = false, type = Command.class )
public class TutorialPlugin3< T extends RealType< T > & NativeType< T >> implements Command {

	@Parameter( label = "3D ImgPlus to be shown." )
	private DatasetView datasetView;

	@Parameter
	private DatasetIOService datasetIO;

	private ImgPlus< T > imgPlus;
	private ImgPlus< ? > imgPlusDots;
	private JFrame frame;

	public TutorialPlugin3() {
		final String filename = "droso_dots.tif";
		final URL iconURL = ClassLoader.getSystemClassLoader().getResource( filename );
		final File file = new File( iconURL.getPath() );

		try {
			if ( file.exists() && file.canRead() ) {
				final Dataset ds = datasetIO.open( file.getAbsolutePath() );
				imgPlusDots = ds.getImgPlus();
			}
		} catch ( final IOException e ) {
			e.printStackTrace();
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void run() {
		imgPlus = ( ImgPlus< T > ) datasetView.getData().getImgPlus();
		frame = new JFrame( "BDV Vistools Tutorial 3" );
		frame.add( new JLabel( "This is our own JFrame!" ), BorderLayout.SOUTH );
		frame.setBounds( 50, 50, 512, 512 );

		// Add a BDV panel to our JFrame
		final BdvHandlePanel bdvHandlePanel = new BdvHandlePanel( frame, Bdv.options() );
		frame.add( bdvHandlePanel.getViewerPanel(), BorderLayout.CENTER );

		frame.setVisible( true );

		// Add an image to our BDV HandlePanel
		final BdvSource bdvSource1 = BdvFunctions.show(
				imgPlus,
				imgPlus.getName(),
				Bdv.options().addTo( bdvHandlePanel ).sourceTransform( 1, 1, 8 ) );

		final BdvSource bdvSourceDrosoDots = BdvFunctions.show(
				imgPlusDots,
				"DOTS",
				Bdv.options().addTo( bdvHandlePanel ).sourceTransform( 1, 1, 8 ) );

		bdvSourceDrosoDots.setDisplayRangeBounds( 0, 255 );
		bdvSourceDrosoDots.setDisplayRange( 0, 255 );
		bdvSourceDrosoDots.setColor( new ARGBType( 0xFF0000 ) );

		bdvSource1.setActive( true );

	}

}
