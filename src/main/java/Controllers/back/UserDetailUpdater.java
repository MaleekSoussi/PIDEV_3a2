package Controllers.back;

import Models.Users;

public interface UserDetailUpdater {
    void updateDetails(Users user); // called when a user item is selected , gets the details and displays them
    void refreshUserList();
    void clearForm();
}
