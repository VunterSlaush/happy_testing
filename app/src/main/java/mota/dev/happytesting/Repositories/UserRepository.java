package mota.dev.happytesting.repositories;

import java.util.List;

import io.reactivex.Observable;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 22/05/2017.
 */

public interface UserRepository
{
    Observable<User> login(String username, String password);
    Observable<List<User>> getUsers();
}
