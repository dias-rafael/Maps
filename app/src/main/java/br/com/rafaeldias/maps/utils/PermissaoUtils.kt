package br.com.rafaeldias.maps.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

object PermissaoUtils {

    fun validaPermissao(permissoes: Array<String>, activity: Activity, requestCode: Int): Boolean {
        if(Build.VERSION.SDK_INT >= 23) {

            val listaPermissao = ArrayList<String>()

            for(permissao in permissoes) {
                val temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED
                if(!temPermissao) listaPermissao.add(permissao)
            }

            if(listaPermissao.isEmpty()) return true
                val novasPermissoes = listaPermissao.toTypedArray()
                ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode)
        }
        return true
    }
}
