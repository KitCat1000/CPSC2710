public class SeatReservation {
   
   private String flightDesignator;
   private java.time.LocalDate flightDate;
   private String firstName;
   private String lastName;
   
   public String getFlightDesignator() {
      return flightDesignator;
   }
   
   public void setFlightDesignator(String fd) {
      if (fd == null || fd.length() < 4 || fd.length() > 6) {
         throw new IllegalArgumentException("Flight designator must be between 4 and 6 characters");
      }
      this.flightDesignator = fd;
   }
         
   public java.time.LocalDate getFlightDate() {
      return flightDate;
   }
   
   public void setFlightDate(java.time.LocalDate date) {
      flightDate = date;
   }
   
   public String getFirstName() {
      return firstName;
   }
   
   public void setFirstName(String fn) {
      firstName = fn;
   }
   
   public String getLastName() {
      return lastName;
   }
   
   public void setLastName(String ln) {
      lastName = ln;
   }
   
   public String toString() {
      return "SeatReservation{" +
                "flightDesignator=" + (flightDesignator == null ? "null" : flightDesignator) +
                ",flightDate=" + (flightDate == null ? "null" : flightDate) +
                ",firstName=" + (firstName == null ? "null" : firstName) +
                ",lastName=" + (lastName == null ? "null" : lastName) +
                '}';
   }
}

