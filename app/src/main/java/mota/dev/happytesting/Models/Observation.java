package mota.dev.happytesting.models;

import java.util.Collection;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Slaush on 22/05/2017.
 */

public class Observation extends RealmObject
{
    private String text;
    private RealmList<Image> images;
}
