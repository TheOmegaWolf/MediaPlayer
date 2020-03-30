package application;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class mainfileController implements Initializable{
	@FXML private MediaView mv;
	private MediaPlayer mp;
	private Media me;
	public boolean playing = false;
	public String URL = "src/media/hall of fame never back down.mp4";
	@FXML Slider volumeSlider;
	@FXML Slider currentpos;
	@FXML 
	private BorderPane borderid;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
	}
	public void buttonscript(ActionEvent event)
	{
		final FileChooser filechooser = new FileChooser();
		Stage stage = (Stage)borderid.getScene().getWindow();
		
		File file = filechooser.showOpenDialog(null);
		
		if(file !=null && playing == false)
		{
			System.out.println("Path : " + file.getAbsolutePath());
			playing = true;
			URL = file.getAbsolutePath().toString();
			String path = new File(URL).getAbsolutePath();
			me = new Media(new File(path).toURI().toString());
			mp = new MediaPlayer(me);
			mv.setMediaPlayer(mp);
			mp.setAutoPlay(true);
			DoubleProperty width = mv.fitWidthProperty();
			DoubleProperty height = mv.fitHeightProperty();
			width.bind(Bindings.selectDouble(mv.sceneProperty(),"width"));
			height.bind(Bindings.selectDouble(mv.sceneProperty(),"height"));
			volumeSlider.setValue(mp.getVolume()*100);
			volumeSlider.valueProperty().addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable observable) {
					// TODO Auto-generated method stub
					mp.setVolume((volumeSlider.getValue())/100);
				}
				});
			
			InvalidationListener sliderChangeListener = o-> {
			    javafx.util.Duration seekTo = javafx.util.Duration.seconds(currentpos.getValue());
			    mp.seek(seekTo);
			};
			currentpos.valueProperty().addListener(sliderChangeListener);

			// Link the player's time to the slider
			mp.currentTimeProperty().addListener(l-> {
			    // Temporarily remove the listener on the slider, so it doesn't respond to the change in playback time
			    // I thought timeSlider.isValueChanging() would be useful for this, but it seems to get stuck at true
			    // if the user slides the slider instead of just clicking a position on it.
			    currentpos.valueProperty().removeListener(sliderChangeListener);

			    // Keep timeText's text up to date with the slider position.
			    javafx.util.Duration currentTime = mp.getCurrentTime();
			    int value = (int) currentTime.toSeconds();
			    currentpos.setValue(value);    

			    // Re-add the slider listener
			    currentpos.valueProperty().addListener(sliderChangeListener);
			});
		}
		
	}
	public void play(ActionEvent event)
	{
		mp.play();
		mp.setRate(1);
		playing = true;
	}
	public void pause(ActionEvent event)
	{
		mp.pause();
		
	}
	public void speed(ActionEvent event)
	{
		mp.setRate(2);
	}
	public void slow(ActionEvent event)
	{
		mp.setRate(0.5);
	}
	public void reload(ActionEvent event)
	{
		mp.seek(mp.getStartTime());
		mp.play();
	}
	public void start(ActionEvent event)
	{
		mp.seek(mp.getStartTime());
		mp.stop();
	}
	public void last(ActionEvent event)
	{
		mp.seek(mp.getTotalDuration());
		mp.stop();	
		playing = false;
	}
	public void normal(ActionEvent event)
	{
		mp.setRate(1);
	}
	
	
}
