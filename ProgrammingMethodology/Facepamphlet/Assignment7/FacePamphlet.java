
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		canvas = new FacePamphletCanvas();
		add(canvas);
		name = new JLabel("Name");
		add(name, NORTH);
		nameTxt = new JTextField(TEXT_FIELD_SIZE);
		add(nameTxt, NORTH);
		addButton = new JButton("Add");
		add(addButton, NORTH);
		deleteButton = new JButton("Delete");
		add(deleteButton, NORTH);
		lookUpButton = new JButton("LookUp");
		add(lookUpButton, NORTH);
		status = new JTextField(TEXT_FIELD_SIZE);
		add(status, WEST);
		changeStatus = new JButton("Change Status");
		add(changeStatus, WEST);
		status.addActionListener(this);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		picture = new JTextField(TEXT_FIELD_SIZE);
		add(picture, WEST);
		picture.addActionListener(this);
		changePicture = new JButton("Change Picture");
		add(changePicture, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		friend = new JTextField(TEXT_FIELD_SIZE);
		add(friend, WEST);
		friend.addActionListener(this);
		addFriend = new JButton("Add Friend");
		add(addFriend, WEST);
		addActionListeners();

	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		String nameText = nameTxt.getText();
		if (e.getSource() == addButton) {
			addProfile(nameText);
		} else if (e.getSource() == deleteButton) {
			deleteProfile(nameText);
		} else if (e.getSource() == lookUpButton) {
			lookUp(nameText);
		} else if (e.getSource() == changeStatus || e.getSource() == status) {
			changeStatus();
		} else if (e.getSource() == changePicture || e.getSource() == picture) {
			changeImage();
		} else if (e.getSource() == addFriend || e.getSource() == friend) {
			addingFriends();
		}
	}

	// this method checks if two persons are friends

	private void checkFriends(String name, boolean areFriends, String friendTxt) {
		Iterator<String> it = profile.getFriends();
		while (it.hasNext()) {
			if (friendTxt.equals(it.next())) {
				areFriends = true;
				updateDisplay(canvas, profile);
				canvas.showMessage(name + " already has " + friendTxt + " as a friend");
				break;
			}
		}
		if (!areFriends) {
			profile.addFriend(friendTxt);
			data.getProfile(friendTxt).addFriend(profile.getName());
			updateDisplay(canvas, profile);
			canvas.showMessage(friendTxt + " added as a friend");
		}
	}

	// this method adds two friends to each other.
	private void addingFriends() {
		String friendTxt = friend.getText();
		boolean areFriends = false;
		if (friendTxt.length() > 0) {
			if (!friendTxt.equals(profile.getName())) {
				if (profile != null) {
					if (data.containsProfile(friendTxt)) {
						checkFriends(profile.getName(), areFriends, friendTxt);
					} else {
						updateDisplay(canvas, profile);
						canvas.showMessage(friendTxt + " does not exist");
					}
				} else {
					canvas.removeAll();
					canvas.showMessage("Please select a profile to add friend");
				}
			} else {
				updateDisplay(canvas, profile);
				canvas.showMessage("Can not add the same person to his friends");
			}
			friend.setText("");
		}
	}

	// this method adds an image to persons profile
	private void changeImage() {
		String imageTxt = picture.getText();
		if (imageTxt.length() > 0) {
			if (profile != null) {
				GImage image = null;
				try {
					image = new GImage(imageTxt);
					profile.setImage(image);
					updateDisplay(canvas, profile);
					canvas.showMessage("Picture updated");
				} catch (ErrorException ex) {
					updateDisplay(canvas, profile);
					canvas.showMessage("Unable to open image file: " + imageTxt);
				}
			} else {
				canvas.removeAll();
				canvas.showMessage("Please select a profile to change picture");
			}
			picture.setText("");

		}
	}

	// this method deletes profile complately if one exists
	private void deleteProfile(String name) {
		if (name.length() > 0) {
			if (data.containsProfile(name)) {
				data.deleteProfile(name);
				profile = null;
				canvas.removeAll();
				canvas.showMessage("Profile of " + name + " deleted");
			} else {
				canvas.showMessage("Profile with the name " + name + " does not exist");
			}
			nameTxt.setText("");
		}
	}

	// this method displays a profile on the screen if one exists
	private void lookUp(String name) {
		if (name.length() > 0) {
			if (data.containsProfile(name)) {
				profile = data.getProfile(name);
				updateDisplay(canvas, profile);
				canvas.showMessage("Displaying " + name);
			} else {
				profile = null;
				canvas.removeAll();
				canvas.showMessage("A profile with the name " + name + " does not exist");
			}
			nameTxt.setText("");
		}
	}

	// this method adds profile
	private void addProfile(String name) {
		if (name.length() > 0) {
			if (!data.containsProfile(name)) {
				profile = new FacePamphletProfile(name);
				data.addProfile(profile);
				updateDisplay(canvas, profile);
				canvas.showMessage("New profile created");
			} else {
				profile = data.getProfile(name);
				updateDisplay(canvas, profile);
				canvas.showMessage("A profile with the name " + profile.getName() + " already exists");
			}
			nameTxt.setText("");
		}
	}

	// this method adds status to profile
	private void changeStatus() {
		String statusTxt = status.getText();
		if (statusTxt.length() > 0) {
			if (profile != null) {
				profile.setStatus(statusTxt);
				updateDisplay(canvas, profile);
				canvas.showMessage("Status updated to " + statusTxt);
			} else {
				canvas.showMessage("Please select a profile to change status");
			}
			status.setText("");
		}

	}

	// this method deletes everything on the screen an then displays profile.
	private void updateDisplay(FacePamphletCanvas canvas, FacePamphletProfile profile) {
		canvas.removeAll();
		canvas.displayProfile(profile);
	}

	private JLabel name;
	private JTextField nameTxt;
	private JButton addButton;
	private JButton deleteButton;
	private JButton lookUpButton;
	private JTextField status;
	private JButton changeStatus;
	private JTextField picture;
	private JButton changePicture;
	private JTextField friend;
	private JButton addFriend;
	private FacePamphletDatabase data = new FacePamphletDatabase();
	private FacePamphletProfile profile;
	private FacePamphletCanvas canvas;
}
