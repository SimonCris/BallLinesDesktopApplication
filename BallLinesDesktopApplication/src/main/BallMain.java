package main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import gameFacade.GameFacade;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.IllegalTermException;

public class BallMain {

	public static void main(String[] args) throws ObjectNotValidException, IllegalAnnotationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException, IllegalTermException, UnsupportedAudioFileException, LineUnavailableException {

		GameFacade game = new GameFacade();
		game.gamePlay();
	}
}
