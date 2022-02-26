package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jtriemstra.bang.api.model.character.BartCassidy;
import com.jtriemstra.bang.api.model.character.BlackJack;
import com.jtriemstra.bang.api.model.character.CalamityJanet;
import com.jtriemstra.bang.api.model.character.Character;
import com.jtriemstra.bang.api.model.character.ElGringo;
import com.jtriemstra.bang.api.model.character.JesseJones;
import com.jtriemstra.bang.api.model.character.Jourdonnais;
import com.jtriemstra.bang.api.model.character.KitCarlson;
import com.jtriemstra.bang.api.model.character.LuckyDuke;
import com.jtriemstra.bang.api.model.character.PaulRegret;
import com.jtriemstra.bang.api.model.character.PedroRamirez;
import com.jtriemstra.bang.api.model.character.RoseDoolan;
import com.jtriemstra.bang.api.model.character.SidKetchum;
import com.jtriemstra.bang.api.model.character.SlabTheKiller;
import com.jtriemstra.bang.api.model.character.SuzyLafayette;
import com.jtriemstra.bang.api.model.character.WillyTheKid;

public class CharacterDeck {

	private List<Character> characters;
	private Random randomizer = new Random();
	
	public CharacterDeck() {
		characters = new ArrayList<>();
		characters.add(new BartCassidy());
		characters.add(new CalamityJanet());
		characters.add(new ElGringo());
		characters.add(new JesseJones());
		characters.add(new Jourdonnais());
		characters.add(new KitCarlson());
		characters.add(new LuckyDuke());
		characters.add(new PaulRegret());
		characters.add(new PedroRamirez());
		characters.add(new RoseDoolan());
		characters.add(new SidKetchum());
		characters.add(new SlabTheKiller());
		characters.add(new SuzyLafayette());
		characters.add(new WillyTheKid());
		characters.add(new BlackJack());
	}
	
	public CharacterDeck(String names) {
		characters = new ArrayList<>();
		
		for (String name : names.split(";")) {
			switch(name) {
			case "bart":
				characters.add(new BartCassidy()); break;
			case "janet":
				characters.add(new CalamityJanet()); break;
			case "gringo":
				characters.add(new ElGringo()); break;
			case "jesse":
				characters.add(new JesseJones()); break;
			case "jourd":
				characters.add(new Jourdonnais()); break;
			case "kit":
				characters.add(new KitCarlson()); break;
			case "duke":
				characters.add(new LuckyDuke()); break;
			case "paul":
				characters.add(new PaulRegret()); break;
			case "pedro":
				characters.add(new PedroRamirez()); break;
			case "rose":
				characters.add(new RoseDoolan()); break;
			case "sid":
				characters.add(new SidKetchum()); break;
			case "slab":
				characters.add(new SlabTheKiller()); break;
			case "suzy":
				characters.add(new SuzyLafayette()); break;
			case "willy":
				characters.add(new WillyTheKid()); break;
			case "jack":
				characters.add(new BlackJack()); break;
			}
		}
	}
	
	public Character draw() {
		return characters.remove(randomizer.nextInt(characters.size()));
	}
}
