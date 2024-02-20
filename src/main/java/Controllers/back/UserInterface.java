package Controllers.back;

import Models.Users;
// this is a callback mechanism
//for the useritemcontroller to call to dashboard controller using these methonds to handle events
//link between the two controllers)
public interface UserInterface {
    void updateDetails(Users user); // called when a user item is selected , gets the details and displays them
    void refreshUserList();
    void clearForm();
    void setSelectedUserID(int userID);
}
