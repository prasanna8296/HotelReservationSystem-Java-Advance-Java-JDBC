//import java.sql.*;
import java.sql.*;
import java.util.*;

import static java.lang.System.exit;

public class HotelReservation {
    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="root";

    public static void main(String[] args) throws ClassNotFoundException,SQLException {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        try{

            Connection con=DriverManager.getConnection(url,username,password);
            Statement st=con.createStatement();
            while(true)
            {
                System.out.println();
                Scanner sc=new Scanner(System.in);
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.println("Choose an option:");
                int choice=sc.nextInt();
                switch (choice)
                {
                    case 1:
                        reserveRoom(con,sc,st);
                        break;
                    case 2:
                        viewReservations(con,st);
                        break;
                    case 3:
                        getRoomNumber(con,sc,st);
                        break;
                    case 4:
                        updateReservation(con,sc,st);
                        break;
                    case 5:
                        deleteReservation(con,sc,st);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again");


                }
            }



        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }





    }

    private static void deleteReservation(Connection con, Scanner sc, Statement st) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = sc.nextInt();

            if (!reservationExists(con, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = con.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }


    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }

    private static void updateReservation(Connection con, Scanner sc, Statement st) {
        try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            if (!reservationExists(con, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = sc.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = con.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getRoomNumber(Connection con, Scanner sc, Statement st) {
        System.out.println("Enter the reservation Id:");
        int id=sc.nextInt();
        System.out.println("Enter the guest name:");
        String name=sc.next();
        String sql="select room_number from reservations "+"where reservation_id="+id+"and guest_name= '"+
                name+"'";
        try{
            ResultSet rs=st.executeQuery(sql);
            if(rs.next())
            {
                int rn=rs.getInt("room_number");
                System.out.println("Room number for Reservation Id"+id+"and guest"+name+"is: "+rn);
            }
            else {
                System.out.println("Reservation for the following id and name not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //reservation_id,guest_name,room_number,contact number,reservation_date
    private static void viewReservations(Connection con, Statement st) {
        String sql="select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";

        try
        {
            ResultSet rs=st.executeQuery(sql);
            System.out.println("Current Reservations");
            System.out.println("-----------------------------------------------------------------------------");
            while (rs.next())
            {
                int id=rs.getInt("reservation_id");
                String name=rs.getString("guest_name");
                int roomNumber=rs.getInt("room_number");
                String contactNumber=rs.getString("contact_number");
                String reservationDate=rs.getTimestamp("reservation_date").toString();

                System.out.printf(String.valueOf(id),name,String.valueOf(roomNumber),String.valueOf(contactNumber),String.valueOf(reservationDate));


            }
            System.out.println("------------------------------------------------------------------------------");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection con, Scanner sc, Statement st) {
        System.out.println("Enter the quest name:");
        String guestname=sc.next();
        sc.nextLine();
        System.out.println("Enter room number");
        int roomNumber=sc.nextInt();
        System.out.println("Enter contact number: ");
        String contactNumber=sc.next();

        String sql="insert into reservations(guest_name,room_number,contact_number)"+
                "values('"+guestname+"'," +roomNumber+",'"+contactNumber+"')";
        try {
            int affectedRows = st.executeUpdate(sql);
            if (affectedRows > 0) {
                System.out.println("Reservation successful!");

            } else {
                System.out.println("Reservation failed!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
