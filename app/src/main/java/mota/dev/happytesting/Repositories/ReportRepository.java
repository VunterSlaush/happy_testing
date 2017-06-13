package mota.dev.happytesting.repositories;

import java.util.List;

import io.reactivex.Observable;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 11/06/2017.
 */

public interface ReportRepository
{
    Observable<List<Report>> getAll();
    Observable<Report> get(int id);
    Observable<Report> create(Report report);
    Observable<Report> modifiy(Report report);
    Observable<Boolean> delete(Report report);
}
