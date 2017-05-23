package mota.dev.happytesting.Repositories;

import io.reactivex.Observable;
import mota.dev.happytesting.Models.User;

/**
 * Created by Slaush on 22/05/2017.
 */

public interface UserRepository
{
    Observable<User> login(String username, String password);

}
