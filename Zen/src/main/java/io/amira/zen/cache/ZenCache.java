package io.amira.zen.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.amira.zen.core.ZenApplication;

/**
 * Created by giovanni on 13/06/14.
 */
public class ZenCache {

    private class AsyncReq {
        private String key;
        private Object caller;
        private String onSuccess;
        private String onError;
        private Object[] params;
        private int time;

        public AsyncReq(String key, Object caller, String onSuccess, String onError, Object[] params) {
            this.key = key;
            this.caller = caller;
            this.onSuccess = onSuccess;
            this.onError = onError;
            this.params = params;
        }

        public void run(Class async_obj, String async_method, int cpos, int spos, int epos, int time) {
            this.time = time;
            int plen;
            if (epos == -1) {
                plen = 2;
            } else {
                plen = 3;
            }
            Class[] cparams = new Class[params.length+plen];
            Object[] cvalues = new Object[params.length+plen];
            int j = 0;
            for (int i = 0; i < params.length+plen; i++) {
                if (i == cpos) {
                    cparams[i] = Object.class;
                    cvalues[i] = this;
                    continue;
                }
                if (i == spos) {
                    cvalues[i] = "_success";
                    cparams[i] = cvalues[i].getClass();
                    continue;
                }
                if (i == epos) {
                    cvalues[i] = "_error";
                    cparams[i] = cvalues[i].getClass();
                    continue;
                }
                cparams[i] = params[j].getClass();
                cvalues[i] = params[j];
                j++;
            }
            try {
                async_obj.getMethod(async_method, cparams).invoke(async_obj, cvalues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void _success(String data) {
            _success((Object) data);
        }

        public void _success(Object data) {
            String sdata = null;
            try {
                if (data != null) {
                    ZenApplication.log("Cache storing");
                    // store in cache
                    sdata = Serializer.toString((Serializable) data);
                    DB.store(key, sdata, time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            async_return(key, caller, onSuccess, data, data.getClass());
        }

        public void _error() { _error(null); }

        public void _error(String data) {
            _success((Object) data);
        }

        public void _error(Object data) {
            async_return(key, caller, onError, data, data.getClass());
        }
    }

    private Map<String, AsyncReq> async_stack;

    public ZenCache() {
        async_stack = new HashMap<String, AsyncReq>();
    }

    public Object _get(String key) {
        Object data = null;
        String sdata = DB.find(key);
        if (sdata != null) {
            try {
                data = Serializer.fromString(sdata);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public void _store(String key, Object data, int minutes) {
        ZenApplication.log("Storing "+key+": "+data);
        try {
            String sdata = Serializer.toString((Serializable) data);
            DB.store(key, sdata, minutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get(String key, Object caller, String method, int minutes) {
        return get(key, caller, method, null, minutes, false);
    }

    public Object get(String key, Object caller, String method, int minutes, boolean refresh) {
        return get(key, caller, method, null, minutes, refresh);
    }

    public Object get(String key, Object caller, String method, Object[] params, int minutes, boolean refresh) {
        Object data = null;
        String sdata = null;
        // check cache
        boolean miss = true;
        if (!refresh) {
            sdata = DB.find(key);
            miss = (sdata == null);
        }
        if (miss) {
            ZenApplication.log("Cache missing");
            try {
                if (params != null) {
                    Class[] cparams = new Class[params.length];
                    for (int i = 0; i < params.length; i++) {
                        cparams[i] = params[i].getClass();
                    }
                    data = caller.getClass().getMethod(method, cparams).invoke(caller, params);
                } else {
                    data = caller.getClass().getMethod(method).invoke(caller);
                }
                if (data != null) {
                    ZenApplication.log("Cache storing");
                    // store in cache
                    sdata = Serializer.toString((Serializable) data);
                    DB.store(key, sdata, minutes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ZenApplication.log("Cache hit");
            try {
                data = Serializer.fromString(sdata);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private void _stack_push(String key, Object caller, String onSuccess, String onError, Object[] params) {
        async_stack.put(key, new AsyncReq(key, caller, onSuccess, onError, params));
    }

    private AsyncReq _stack_get(String key) {
        return async_stack.get(key);
    }

    private void _stack_rem(String key) {
        async_stack.remove(key);
    }

    public void async(String key, Class async_obj, String async_method, Object[] async_params,
                      int async_arg_caller, int async_arg_method, Object caller,
                      String on_end_method, int minutes) {
        async(key, async_obj, async_method, async_params, async_arg_caller, async_arg_method,
                caller, on_end_method, minutes, false);
    }

    public void async(String key, Class async_obj, String async_method, Object[] async_params,
                      int async_arg_caller, int async_arg_method, Object caller,
                      String on_end_method, int minutes, boolean refresh) {
        async(key, async_obj, async_method, async_params, async_arg_caller, async_arg_method, -1,
                caller, on_end_method, null, minutes, refresh);
    }

    public void async(String key, Class async_obj, String async_method, Object[] async_params,
                      int async_arg_caller, int async_arg_mSuccess, int async_arg_mError,
                      Object caller, String onSuccessMethod, String onErrorMethod,
                      int minutes) {
        async(key, async_obj, async_method, async_params, async_arg_caller, async_arg_mSuccess,
                async_arg_mError, caller, onSuccessMethod, onErrorMethod, minutes, false);
    }

    public void async(String key, Class async_obj, String async_method, Object[] async_params,
                      int async_arg_caller, int async_arg_mSuccess, int async_arg_mError,
                      Object caller, String onSuccessMethod, String onErrorMethod,
                      int minutes, boolean refresh) {
        Object data;
        String sdata = null;
        // check cache
        boolean miss = true;
        if (!refresh) {
            sdata = DB.find(key);
            miss = (sdata == null);
        }
        if (miss) {
            ZenApplication.log("Cache miss");
            try {
                _stack_push(key, caller, onSuccessMethod, onErrorMethod, async_params);
                _stack_get(key).run(async_obj, async_method, async_arg_caller, async_arg_mSuccess,
                        async_arg_mError, minutes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ZenApplication.log("Cache hit");
            try {
                data = Serializer.fromString(sdata);
                async_return(key, caller, onSuccessMethod, data, data.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void async_return(String key, Object caller, String method, Object data, Class data_class) {
        try {
            Class[] cparams = new Class[1];
            cparams[0] = data_class;
            Object[] cvalues = {data};
            caller.getClass().getMethod(method, cparams).invoke(caller, cvalues);
            _stack_rem(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clean_old() {
        DB.clean();
    }

    public void erase() {
        DB.flush();
    }

}
