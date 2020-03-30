## Inspiration
COVID-19 has been spreading immensely, even though measures have been put in place to reduce the spread. If only there was a way to know who a person just tested positive has been in contact with recently, the government/care providers could provide even preventive/ special care and help them take precautions.

## What it does
Once the user turns on the app and hits start ("Indicates he is going somewhere out side") the app starts periodically transmitting as a beacon and receiving signals for a set amount of time. When it detects another beacon nearby, it stores the data such as bluetooth address transmitted, distance, and mainly location and time it detected the other beacon.

When a health care provider needs to know who a person just tested positive for COVID-19 has been in contact recently, he/she can login with their admin credentials on the Trail web server, and put in the UserAccount and access code given from the app. The server matches  **"Time" and geofenced "Location"** to generate a list of people in that area at that time. This **matching of admin credentials with app access code and user account protects privacy.**

## How I built it
I used it by going through doc of AltBeacon, Google, and the libraries I have used in the application. The app uses WorkManager to schedule work in the background. The user does not have to keep the app open for it to work.

## Challenges I ran into
I had never worked with bluetooth, and beacon technologies, it was really tough understanding the process of ranging and transmitting, and making them work correctly in the App.

## Accomplishments that I'm proud of
I was able to build the app side of the project, and make the transmitting and ranging work correctly while storing the data in the application. I hope this effort helps, at least in some way, with fighting the COVID-19.

## What I learned
How beacons work, how the bluetooth technologies in android are privacy protected as they transmit different bluetooth address every-time it transmits

## Current limitations
Currently the app is set up to run for 2 minutes (1 min transmit and 1 min range) which will be later changed to run for specified time period. The data is saved locally right now which will later be uploaded automatically to server for full functionality.

## What's next for Trail
- Building the server which generates the list of people in contact. Optimizing the application, and maybe incorporating the Google Nearby API.
- Adding a warning system, which warns people entering a crowded area.

