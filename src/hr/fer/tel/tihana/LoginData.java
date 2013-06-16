package hr.fer.tel.tihana;

public class LoginData {

	public static String username="",password="";
	public static int idUser;
	

	public static int getIdUser() {
		return idUser;
	}

	public static void setIdUser(int idUser) {
		LoginData.idUser = idUser;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		LoginData.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		LoginData.password = password;
	}	
}
