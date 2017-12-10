package visualizareceita;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.R;

/**
 * Behaviour usada no XML, especificamente no Viewpager na tela de visualizar a receita,
 * que faz o pager "ficar conectado" em baixo do tablayout .
 * Created by klaus on 09/12/17.
 */

public class VisualizaPassoXMLBehaviour extends CoordinatorLayout.Behavior<ViewPager> {
    public VisualizaPassoXMLBehaviour(){

    }

    public VisualizaPassoXMLBehaviour(Context context, AttributeSet atts){
        super(context,atts);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,ViewPager child, View dep){
        return false;
    }

    void updateTranslationForScrollView(){

    }
    @Override
    public boolean onLayoutChild (CoordinatorLayout parent,
                   ViewPager child,
                   int layoutDirection) {
        View dep = parent.findViewById(R.id.toolbar3);

        if (dep instanceof android.support.v7.widget.Toolbar) {
            int cord[] = new int [2];
            dep.getLocationInWindow (cord);

            android.support.v7.widget.Toolbar ct =  (android.support.v7.widget.Toolbar )dep;
            int y  = cord[1] + ct.getHeight();
            child.setY(y);
            ViewGroup.LayoutParams params = child.getLayoutParams();
            params.height = parent.getHeight() - y;
            child.setLayoutParams(params);
            parent.findViewById(R.id.scroll_view).setBottom(y);



        }
        return false;

    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewPager child, View dep) {
        dep = parent.findViewById(R.id.toolbar3);

        if (dep instanceof android.support.v7.widget.Toolbar) {
            int cord[] = new int [2];
            dep.getLocationInWindow (cord);

                    android.support.v7.widget.Toolbar ct =  (android.support.v7.widget.Toolbar )dep;
                    int y  = cord[1] + ct.getHeight();
                    child.setY(y);
                    ViewGroup.LayoutParams params = child.getLayoutParams();
                    params.height = parent.getHeight() - y;
                    child.setLayoutParams(params);
                    parent.findViewById(R.id.scroll_view).setBottom(y);



        }
        return true;
    }



}
