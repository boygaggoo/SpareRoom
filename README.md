# Android Example Application

This is an Spare Room Android Application README to show briefly the sections your app README should contain.

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/boygaggoo/SpareRoom.git
```

## Configuration
### Keystores:
Create `app/keystore.gradle` with the following info: (Not Available)
```gradle
ext.key_alias='...'
ext.key_password='...'
ext.store_password='...'
```
And place both keystores under `app/keystores/` directory:
- `playstore.keystore`
- `stage.keystore`


## Build variants
Use the Android Studio *Build Variants* button to choose between **release** and **debug** flavors combined with debug and release build types


## Design Pattern 
	- MVI (Model View Intent)

## DataBase
	- Room

## NetWorking 
	- Retrofit with Kotlin Coroutine 
## Unit Tests	
	- Junit

## App Feature
	- Internet Check
	- Dark Mode
	- Local Database
	- Pull to Refresh
	- Clean Code Architecture

## App Demo

[![Loading](https://raw.githubusercontent.com/boygaggoo/SpareRoom/master/demo.gid)]()

## Maintainers
This project is mantained by:
* [Muhammad Zia Shahid](http://github.com/boygaggoo)


## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Run the Project.
5. Push your branch (git push origin my-new-feature)
6. Create a new Pull Request
