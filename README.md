# Movies

The architecture are MVVM and DI
  1. Using DI in order to inject the objects outside the class.
  2. Using MVVM to speartae the UI/View from the bussines logic and support reactive programing(via RxJava)
  
The most diffuclt in the project was to load the movies and the images in the same time.

Project schema:

LAYOUT:
   SplashActivity- Load the data project

   BaseMoviesActivity (abstract class) that handles the movies list and the action button.
         - MoviesFavListActivity inheritance from BaseMoviesActivity,
           pass the relevant VM and handle the user movies list
         - MoviesListActivity inheritance from BaseMoviesActivity,
           pass the relevant VM and handle the main movies list
            
    MovieDetailsFragment- Show the details of the movie after the user clicks on it
       
       
REPOSITORY:
  MoviesFavoritesRepository- Handle favoriets movies
  
  ImagesRepository- Handle movies' images
  
  MoviesRepository- Handle movies' data
  
  
Managers:
  BaseNetworkManager- Handle and create the Retrofit
  
  MoviesNetworkManager- Handle the request to get movies' data from the server
  
  
There are not known bugs.

To improve the project need to handle errors from the server
and create offline mode.
  
            
  
