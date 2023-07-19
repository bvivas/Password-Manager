# Password-Manager
Password Manager in Android for Seguridad y Auditoría de los Sistemas de Información project at EPS-UAM.

## Installation
To test the app, it will be necessary to have Android Studio installed, and configure the emulator.

To open the project, start Android Studio and select *File -> Open Project*. The file to open is `PasswordManager`.

### Create virtual device on Android Studio
To create a virtual device in the emulator:
1. Start the device manager (also named as AVD).
2. Select *Create device*.
3. In the installer wizard, select a device (the app has been tested on a Pixel 3a XL) with a x86 system image and a level 30 API.
4. Enter a name for the virtual device.

Once the device is created, start it and enable the developer options going to *Settings -> About phone (or about device)* and tap several times on the *Build number* option.

### Run the app
1. Build the project with *Build -> Make Project*.
2. Start the app with *Run*.

While using the application, the database will be accessible in the *App inspection* tab.

## Usage
Once the app is started, you will be prompted to the Log In screen.
### Log In
To log into the application, you need to have an account. To create an account, go to *Sign Up*.  

You must log in with an username and a password, and tap on *Log In*.  

![login demo](https://github.com/bvivas/Password-Manager/blob/master/media/login.gif)


### Sign Up
To create an account, you need to enter an username and a password. Enter the password twice.  

![signup demo](https://github.com/bvivas/Password-Manager/blob/master/media/signup.gif)

### Sign Out

To sign out, go to the rightmost menu and tap on *Log out*.

![signout demo](https://github.com/bvivas/Password-Manager/blob/master/media/sign-out.gif)

### Delete user account

This action cannot be undone. Once you delete your user account, the system will erase all your user data and sites along with your passwords. To delete it, got to the rightmost menu and tap on *Delete account*.

![delete user account demo](https://github.com/bvivas/Password-Manager/blob/master/media/delete-user-acc.gif)

### Add new site

You can add a new site specifying its name, your username on that site and your password. You can autogenerate a strong password tapping on *Autogenerate password*. This feature can be found in the middle menu.

![add new site demo](https://github.com/bvivas/Password-Manager/blob/master/media/newsite.gif)

### Delete a site

You can delete a site that you no longer need tapping on its card and then on *Delete site*. This action will delete your password on that site.

![delete site demo](https://github.com/bvivas/Password-Manager/blob/master/media/delete-site.gif)

### Look up a password

To look up a specific password, tap on the desired site card and then on the eye icon in the password field. This will unhide it.

![lookup demo](https://github.com/bvivas/Password-Manager/blob/master/media/lookup.gif)
