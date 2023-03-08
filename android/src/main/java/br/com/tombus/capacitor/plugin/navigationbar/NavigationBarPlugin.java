package br.com.tombus.capacitor.plugin.navigationbar;

import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;


import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.regex.Pattern;

@CapacitorPlugin(name = "NavigationBar")
public class NavigationBarPlugin extends Plugin {
    private NavigationBar implementation = new NavigationBar();

    @PluginMethod
    public void hide(PluginCall call) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window = getActivity().getWindow();
                WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.getDecorView());
                windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.navigationBars());

                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                notifyListeners("onHide", new JSObject());
                call.resolve();
            }
        });
    }

    @PluginMethod
    public void show(PluginCall call) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window = getActivity().getWindow();
                WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.getDecorView());
                windowInsetsControllerCompat.show(WindowInsetsCompat.Type.navigationBars());

                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                notifyListeners("onShow", new JSObject());
                call.resolve();
            }
        });
    }

    @PluginMethod
    public void setTransparency(PluginCall call) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Boolean isTransparent = call.getBoolean("isTransparent");
                    Window window = getActivity().getWindow();
                    if(isTransparent) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            WindowCompat.setDecorFitsSystemWindows(window, false);
                            window.setNavigationBarColor(Color.TRANSPARENT);
                            window.setNavigationBarContrastEnforced(false);
                        } else {
                            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                        }
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    }

                    call.resolve();
                } else {
                    call.unavailable("Not available on Android API 18 or earlier.");
                }
            }
        });
    }

    @PluginMethod
    public void setColor(PluginCall call) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String color = call.getString("color");
                Boolean darkButtons = call.getBoolean("darkButtons");

                if(color == null || color.isEmpty()) {
                    color = "#FFFFFF";
                }

                if(!this.validateHexColor(color)) {
                    call.reject("Invalid Hexadecimal color");
                    return;
                }

                if(darkButtons == null) {
                    darkButtons = false;
                }

                Window window = getActivity().getWindow();
                WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.getDecorView());

                windowInsetsControllerCompat.setAppearanceLightNavigationBars(darkButtons);

                window.setNavigationBarColor(Color.parseColor(color));
                String newColor = String.format("#%08X", (0xFFFFFFFF & window.getNavigationBarColor()));

                if(newColor.contains("#FF")) {
                    newColor = newColor.replace("#FF", "#");
                }

                JSObject ret = new JSObject();
                ret.put("color", newColor);
                notifyListeners("onColorChange", ret);

                call.resolve();
            }

            /*
            * Support Hex values with alpha and without alpha
            * */
            private boolean validateHexColor(String color) {
                if (color == null || color.isEmpty()) {
                    return false;
                }

                Pattern hexPattern = Pattern.compile("^#([A-Fa-f0-9]{6})$|^#([A-Fa-f0-9]{8})$");
                return hexPattern.matcher(color).matches();
            }
        });
    }

    @PluginMethod
    public void getColor(PluginCall call) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window = getActivity().getWindow();
                String color = String.format("#%08X", (0xFFFFFFFF & window.getNavigationBarColor()));

                if(color.contains("#FF")) {
                    color = color.replace("#FF", "#");
                }

                JSObject colorObject = new JSObject();
                colorObject.put("color", color);
                call.resolve(colorObject);
            }
        });
    }
}
