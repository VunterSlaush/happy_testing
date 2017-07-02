package mota.dev.happytesting.repositories;

import java.util.List;

import io.reactivex.Observable;
import mota.dev.happytesting.models.Image;

/**
 * Created by Slaush on 18/06/2017.
 */

public interface ImageRepository
{
    Observable<List<Image>> getAll();
    Observable<List<Image>> getObservationImages(String observationId);
}
