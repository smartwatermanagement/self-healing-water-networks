package utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kempa on 13/11/14.
 */
public class StorageArrayAdapter extends ArrayAdapter<String>{

    private Map<Integer, Integer> storageIDMap = new HashMap<Integer, Integer>();

    public StorageArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(String name, int storageId) {
        super.add(name);
        notifyDataSetChanged();
        storageIDMap.put(getPosition(name), storageId);
    }

    @Override
    public void add(String name) {
        try {
            throw new StorageArrayAdapterAddException();
        } catch (StorageArrayAdapterAddException e) {
            e.printStackTrace();
        }
    }

    public int getStorageId(int position) {
        return storageIDMap.get(position);
    }
}

class StorageArrayAdapterAddException extends Exception {
    public StorageArrayAdapterAddException() {
        super("You need to specify the id of the storage. Use add(String, int) instead.");
    }
}


