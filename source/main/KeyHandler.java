package source.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	public boolean upPressed, downPressed, leftPressed, rightPressed;

	@Override
	public void keyTyped(KeyEvent event) {
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int code = event.getKeyCode();

		if (code == KeyEvent.VK_Z) {
			upPressed = true;
		}

		if (code == KeyEvent.VK_Q) {
			leftPressed = true;
		}

		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}

		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		int code = event.getKeyCode();

		if (code == KeyEvent.VK_Z) {
			upPressed = false;
		}

		if (code == KeyEvent.VK_Q) {
			leftPressed = false;
		}

		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}

		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
	}
	
}
