package de.teamFive.authentication;


import de.teamFive.common.exception.InvalidUsernameOrPasswordException;
import de.teamFive.common.exception.UsernameDoesAlreadyExistException;
import de.teamFive.common.user.User;
import de.teamFive.common.user.UserDAO;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

@Stateless
public class AuthenticationService {

    @EJB
    private UserDAO userDAO;
    @EJB
    private AuthenticationDAO authDAO;


    /**
     * Authenticates a user by searching for a {@link User} whose username corresponds with the given username and
     * validates that the given password matches the password saved for that user.
     *
     * @param username
     * @param password
     * @return successfully authenticated {@link User}
     */
    public User authenticate(String username, String password) {
        Authentication auth = authDAO.getAuthenticationByUsername(username).orElseThrow(InvalidUsernameOrPasswordException::new);
        if (Arrays.equals(auth.getHashedPassword(), hashPassword(password, auth.getSalt()))) {
            return auth.getUser();
        } else {
            throw new InvalidUsernameOrPasswordException();
        }
    }

    /**
     * Registers new user by creating and saving new {@link User}.
     * Additionally a {@link Authentication}  will be created, saved and returned.
     *
     * @param username
     * @param password
     * @return {@link Authentication} with was saved into the corresponding repository
     */
    public Authentication register(String username, String password) {
        if (userDAO.doesExist(username))
            throw new UsernameDoesAlreadyExistException();

        User user = userDAO.save(new User(null, username, new ArrayList<>()));
        byte[] salt = generateSalt();
        return authDAO.save(new Authentication(null, user, hashPassword(password, salt), salt));
    }

    /**
     * Generates a salt used during the hashing process of registration and authentication
     *
     * @return byte[] holding the salt
     */
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes the given password using the SHA-512 hashing-algorithm
     *
     * @param password
     * @param salt
     * @return byte[] holding the hashed password
     */
    @SneakyThrows
    private byte[] hashPassword(String password, byte[] salt) {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);

        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }

}
