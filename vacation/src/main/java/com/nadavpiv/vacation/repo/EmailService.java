package com.nadavpiv.vacation.repo;

import com.nadavpiv.vacation.model.Vacation;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public String sendEmail(String toEmail, String subject, Vacation vacation) {
        Email from = new Email("vacationplanner@walla.co.il"); // Replace with your verified email
        Email to = new Email(toEmail);

        // Create the HTML content with headers, bold text, and properly formatted restaurant details
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html>")
                .append("<body>")
                .append(buildSummaryContent(vacation))
                .append(buildFlightDetails(vacation))
                .append(buildHotelDetails(vacation))
                .append(buildRestaurantDetails(vacation))
                .append(buildDaysAndAttractions(vacation))
                .append("</body>")
                .append("</html>");

        Content content = new Content("text/html", emailContent.toString());  // Set content type to HTML
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        // Send the email
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            return "Status Code: " + response.getStatusCode() +
                    ", Body: " + response.getBody() +
                    ", Headers: " + response.getHeaders();
        } catch (IOException ex) {
            return "Error sending email: " + ex.getMessage();
        }
    }
    private StringBuilder buildSummaryContent(Vacation vacation){
        StringBuilder emailSummary = new StringBuilder();
        emailSummary.append("<h1>Vacation Details</h1>")
                .append("<p><strong>Travel Name:</strong> ").append(vacation.getTravelName()).append("</p>")
                .append("<p><strong>Country:</strong> ").append(vacation.getCountry()).append("</p>")
                .append("<p><strong>Month:</strong> ").append(vacation.getMonth()).append("</p>")
                .append("<p><strong>City:</strong> ").append(vacation.getCity()).append("</p>")
                .append("<p><strong>Number of People:</strong> ").append(vacation.getPassengers()).append("</p>")
                .append("<p><strong>Price:</strong> ").append(vacation.getPrice()).append('$').append("</p>");
        return emailSummary;
    }
    private StringBuilder buildFlightDetails(Vacation vacation){
        StringBuilder emailFlight = new StringBuilder();
        emailFlight.append("<h2>Flight Details</h2>")
                .append("<p><strong>Company:</strong> ").append(vacation.getFlight().getCompanyName()).append("</p>")
                .append("<p><strong>Inbound Flight:</strong> ").append(vacation.getFlight().getInboundFlight()).append("</p>")
                .append("<p><strong>Outbound Flight:</strong> ").append(vacation.getFlight().getOutboundFlight()).append("</p>")
                .append("<p><strong>Flight Price:</strong> ").append(vacation.getFlight().getFlightPrice()).append('$').append("</p>");
        return emailFlight;
    }
    private StringBuilder buildHotelDetails(Vacation vacation){
        StringBuilder emailHotel = new StringBuilder();
        emailHotel.append("<h2>Hotel Details</h2>")
                .append("<p><strong>Hotel Name:</strong> ").append(vacation.getHotel().getHotelName()).append("</p>")
                .append("<p><strong>Hotel Price:</strong> ").append(vacation.getHotel().getHotelPrice()).append('$').append("</p>");
        return emailHotel;
    }
    private StringBuilder buildRestaurantDetails(Vacation vacation){
        StringBuilder emailRestaurant = new StringBuilder();
        emailRestaurant.append("<h2>Restaurant Details</h2>");
        if (vacation.getRestaurants() != null && !vacation.getRestaurants().isEmpty()) {
            for (int i = 0; i < Math.min(vacation.getRestaurants().size(), 5); i++) {
                emailRestaurant.append("<p><strong>Restaurant Name:</strong> ").append(vacation.getRestaurants().get(i).getRestaurantName()).append("<br>")
                        .append("<strong>Restaurant Address:</strong> ").append(vacation.getRestaurants().get(i).getRestaurantAddress()).append("<br>")
                        .append("<strong>Price per person:</strong> ").append(vacation.getRestaurants().get(i).getRestaurantPricePerPerson()).append('$').append("</p>");
            }
        } else {
            emailRestaurant.append("<p>No restaurant details available.</p>");
        }
        return emailRestaurant;
    }
    private StringBuilder buildDaysAndAttractions(Vacation vacation){
        StringBuilder emailDaysAttractions = new StringBuilder();
        emailDaysAttractions.append("<h2>Itinerary</h2>");
        if (vacation.getDays() != null && !vacation.getDays().isEmpty()) {
            for (int i = 0; i < vacation.getDays().size(); i++) {
                emailDaysAttractions.append("<h3>Day ").append(i + 1).append("</h3>")
                        .append("<p><strong>Description:</strong> ").append("</p>");
                if (vacation.getDays().get(i).getAttractions() != null && !vacation.getDays().get(i).getAttractions().isEmpty()) {
                    emailDaysAttractions.append("<ul>");
                    for (int j = 0; j < vacation.getDays().get(i).getAttractions().size(); j++) {
                        emailDaysAttractions.append("<li>")
                                .append("<strong>Attraction Name:</strong> ").append(vacation.getDays().get(i).getAttractions().get(j).getAttractionName()).append("<br>")
                                .append("<strong>Attraction Details:</strong> ").append(vacation.getDays().get(i).getAttractions().get(j).getAttractionDetails()).append("<br>")
                                .append("<strong>Attraction Price:</strong> ").append(vacation.getDays().get(i).getAttractions().get(j).getAttractionPrice()).append('$').append("<br><br>")
                                .append("</li>");
                    }
                    emailDaysAttractions.append("</ul>");
                } else {
                    emailDaysAttractions.append("<p>No attractions available.</p>");
                }
            }
        } else {
            emailDaysAttractions.append("<p>No itinerary details available.</p>");
        }

        return emailDaysAttractions;
    }
}
