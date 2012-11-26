package com.example.listviewtest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView lvListe;
	
	//biblioth�que de livres
	List<Livre> maBibliotheque = new ArrayList<Livre>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //r�cup�ration de la liste view
        lvListe = (ListView)findViewById(R.id.lvListe);
        
        //donn�es de la liste
        //String[] lisStrings = {"France", "Allemagne", "Russie"};
        
        //source de donn�es
        RemplirLaBibliotheque();
        LivreAdapter dataSource = new LivreAdapter(this, maBibliotheque);
        
        lvListe.setAdapter(dataSource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    //remplissage de la bibliotheque de livre
    private void RemplirLaBibliotheque()
    {
    	
    	maBibliotheque.clear();
    	
    	maBibliotheque.add(new Livre("Starcraft 2 : Les diables du ciel", "Willian-C Dietz"));
    	
    	maBibliotheque.add(new Livre("L'art du d�veloppement Android", "Mark Murphy"));
    	
    	maBibliotheque.add(new Livre("Le seuil des  t�n�bres", "Karen Chance"));
    }
}
