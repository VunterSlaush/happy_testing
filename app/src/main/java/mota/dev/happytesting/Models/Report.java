package mota.dev.happytesting.models;

import java.util.Collection;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Slaush on 22/05/2017.
 */

public class Report extends RealmObject
{
    private int id;
    private RealmList<Observation> observations;
}
