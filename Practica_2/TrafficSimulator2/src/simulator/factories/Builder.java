package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Builder<T> {
	protected String _type;

	Builder(String type) {
		if (type == null)
			throw new IllegalArgumentException("Invalid type: " + type);
		else
			_type = type;
	}

	public T createInstance(JSONObject info) {

		T b = null;

		if (_type != null && _type.equals(info.getString("type"))) {
                    try{
			b = createTheInstance(info.has("data") ? info.getJSONObject("data") : null);
                    } catch(JSONException e){
                        b=null;
                    }
		}

		return b;
	}

	protected abstract T createTheInstance(JSONObject data) throws JSONException;
}
