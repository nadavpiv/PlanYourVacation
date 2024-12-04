# Vacation Planner Application
This project is a vacation planner application that allows users to input their vacation preferences and receive personalized vacation plans. The backend is developed using **Spring Boot**, and the frontend is built with **React.js**, the application containerized using **Docker** and deployed via **Google Cloud**.  
The app retrieves vacations from a **MongoDB** database that match the user's preferences and then uses the **ChatGPT API** to generate a new, personalized vacation itinerary.  
**Google Places API** is integrated to fetch photos of attractions from the generated itinerary, and **SendGrid API** is used to email vacation details to users.  
**Google OAuth 2.0** is implemented for secure user authentication.
## Key features:
- Fetches vacation options from a **MongoDB** database based on user input.
- Uses the **ChatGPT API** to generate additional personalized vacation itineraries.
- Integrates **Google Places API** to display attraction photos.
- Sends vacation details via email using **SendGrid API**.
- Secure user authentication with **Google OAuth 2.0**.
- Scalable deployment with **Docker**.
