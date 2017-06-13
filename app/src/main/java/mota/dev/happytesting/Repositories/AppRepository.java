package mota.dev.happytesting.repositories;

import java.util.List;

import io.reactivex.Observable;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 29/05/2017.
 */

public interface AppRepository
{
    Observable<App> create(String name, List<User> users);
    Observable<List<App>> getAll();
    Observable<App> get(int id);
    Observable<App> modifiy(App app);
    Observable<Boolean> delete(App app);
    void updateApps(List<App> apps);
}
