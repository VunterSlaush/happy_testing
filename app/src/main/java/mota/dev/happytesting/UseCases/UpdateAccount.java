package mota.dev.happytesting.useCases;



import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.managers.UserManager;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.UserRepository;
import mota.dev.happytesting.repositories.implementations.UserRemoteImplementation;

/**
 * Created by Slaush on 20/06/2017.
 */

public class UpdateAccount
{
    public User getUserData()
    {
        return UserManager.getInstance().getUserIfExist(MyApplication.getInstance());
    }

    public Observable<User> updateUserData(String name, String username,
                                           final String password)
    {
        final User user = UserManager.getInstance().createUser(name,username,password);
        return new Observable<User>()
        {
            @Override
            protected void subscribeActual(final Observer<? super User> observer)
            {
                UserRepository repo = new UserRemoteImplementation();
                repo.modify(user).subscribe(new Consumer<User>() {
                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        user.setPassword(password); // esto es porque la password llega encriptada!
                        UserManager.getInstance().saveUserCredentials(MyApplication.getInstance(), user);
                        observer.onNext(user);
                        observer.onComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        observer.onError(throwable);
                    }
                });
            }
        };
    }


}
