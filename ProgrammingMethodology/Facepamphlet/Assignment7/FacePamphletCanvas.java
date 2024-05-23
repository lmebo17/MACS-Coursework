
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		GLabel lab = new GLabel(msg);
		lab.setFont(MESSAGE_FONT);
		add(lab, getWidth() / 2 - lab.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		displayName(profile);
		displayImage(profile);
		displayStatus(profile);
		displayFriends(profile);
	}

	// this method displays friends
	private void displayFriends(FacePamphletProfile profile) {
		GLabel friends = new GLabel("Friends:");
		friends.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friends, getWidth() / 2, offsetFromLabel + IMAGE_MARGIN);
		Iterator<String> it = profile.getFriends();
		int count = 0;
		while (it.hasNext()) {
			count++;
			String friend = it.next();
			GLabel lab = new GLabel(friend);
			lab.setFont(PROFILE_FRIEND_FONT);
			add(lab, getWidth() / 2, offsetFromLabel + IMAGE_MARGIN + count * friends.getHeight());
		}
	}

	// this method displays status
	private void displayStatus(FacePamphletProfile profile) {
		GLabel lab;
		if (profile.getStatus().equals("")) {
			lab = new GLabel("No current status");
		} else {
			String status = profile.getStatus();
			lab = new GLabel(profile.getName() + " is " + status);
		}
		lab.setFont(PROFILE_STATUS_FONT);
		add(lab, LEFT_MARGIN, offsetFromLabel + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN + lab.getAscent());
	}

	// this method displays image
	private void displayImage(FacePamphletProfile profile) {
		if (profile.getImage() == null) {
			GRect rect = new GRect(LEFT_MARGIN, IMAGE_MARGIN + offsetFromLabel, IMAGE_WIDTH, IMAGE_HEIGHT);
			GLabel lab = new GLabel("No Image");
			lab.setFont(PROFILE_IMAGE_FONT);
			double labX = LEFT_MARGIN + (IMAGE_WIDTH - lab.getWidth()) / 2;
			double labY = IMAGE_MARGIN + offsetFromLabel + IMAGE_HEIGHT / 2;
			add(rect);
			add(lab, labX, labY);
		} else {
			GImage img = profile.getImage();
			img.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(img, LEFT_MARGIN, IMAGE_MARGIN + offsetFromLabel);

		}

	}

	// this method displays name
	private void displayName(FacePamphletProfile profile) {
		String name = profile.getName();
		GLabel nameLab = new GLabel(name);
		nameLab.setColor(Color.BLUE);
		nameLab.setFont(PROFILE_NAME_FONT);
		add(nameLab, LEFT_MARGIN, TOP_MARGIN + nameLab.getAscent());
		offsetFromLabel = TOP_MARGIN + nameLab.getAscent();
	}

	private double offsetFromLabel = 0;

}
