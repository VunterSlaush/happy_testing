package mota.dev.happytesting.repositories;

import java.util.List;

import io.reactivex.Observable;
import mota.dev.happytesting.models.App;

/**
 * Created by Slaush on 29/05/2017.
 */

public interface AppRepository
{
    Observable<App> create(String name);
    Observable<List<App>> getAll();
    Observable<App> modifiy(App app);
    Observable<Boolean> delete(App app);
    void updateApps(List<App> apps);
}
