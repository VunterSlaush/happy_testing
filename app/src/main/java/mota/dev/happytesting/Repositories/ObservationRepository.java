package mota.dev.happytesting.repositories;

import java.util.List;

import io.reactivex.Observable;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 25/06/2017.
 */

public interface ObservationRepository
{
    Observable<Observation> create(String text, Report report);
    Observable<List<Observation>> getReportObservations(Report report);
    Observable<Observation> get(int id, String localId);
    Observable<Observation> modify(Observation o);
    Observable<Boolean> delete(Observation o);
}
