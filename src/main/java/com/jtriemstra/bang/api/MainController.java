package com.jtriemstra.bang.api;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jtriemstra.bang.api.dto.request.BarrelRequest;
import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.ChooseCardRequest;
import com.jtriemstra.bang.api.dto.request.ChooseTargetRequest;
import com.jtriemstra.bang.api.dto.request.CreateRequest;
import com.jtriemstra.bang.api.dto.request.DefenseOptionsRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRuleRequest;
import com.jtriemstra.bang.api.dto.request.DiscardSidKetchumRequest;
import com.jtriemstra.bang.api.dto.request.DismissMessageRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.request.DrawSourceRequest;
import com.jtriemstra.bang.api.dto.request.JoinRequest;
import com.jtriemstra.bang.api.dto.request.PassRequest;
import com.jtriemstra.bang.api.dto.request.PlayRequest;
import com.jtriemstra.bang.api.dto.request.StartRequest;
import com.jtriemstra.bang.api.dto.request.WaitingRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.InitResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.GameFactory;
import com.jtriemstra.bang.api.model.GameList;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins = {"http://localhost:8001"})
public class MainController {

	@Autowired
	GameList games;
	
	@Autowired
	GameFactory gameFactory;
	
	@PostMapping("/drawSource")
	public BaseResponse drawSource(@RequestBody DrawSourceRequest request) {
		log.info("drawSource");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	@PostMapping("/draw")
	public BaseResponse draw(@RequestBody DrawRequest request) {
		log.info("draw");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	
	@PostMapping("/chooseCard")
	public BaseResponse chooseCard(@RequestBody ChooseCardRequest request) {
		log.info("chooseCard");
		  
		Game game = getFromRequest(request); 
		return game.getPlayerById(request.getPlayerId()).doAction(request); 
	}
	 
	
	@PostMapping("/wait")
	public BaseResponse wait(@RequestBody WaitingRequest request) {
		log.info("wait");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}	 
	
	@PostMapping("/barrel")
	public BaseResponse barrel(@RequestBody BarrelRequest request) {
		log.info("barrel");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	@PostMapping("/play")
	public BaseResponse play(@RequestBody PlayRequest request) {
		log.info("play");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	@PostMapping("/chooseTarget")
	public BaseResponse chooseTarget(@RequestBody ChooseTargetRequest request) {
		log.info("chooseTarget");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}

	@PostMapping("/defenseOptions")
	public BaseResponse defenseOptions(@RequestBody DefenseOptionsRequest request) {
		log.info("defenseOptions");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}

	@PostMapping("/pass")
	public BaseResponse pass(@RequestBody PassRequest request) {
		log.info("pass");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	@PostMapping("/discardRule")
	public BaseResponse discardRule(@RequestBody DiscardRuleRequest request) {
		log.info("discardRule");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	@PostMapping("/discard")
	public BaseResponse discard(@RequestBody DiscardRequest request) {
		log.info("discard");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	@PostMapping("/join")
	public InitResponse joinGame(@RequestBody JoinRequest request) {
		log.info("joinGame");
		
		Game game = getFromRequest(request); 
		game.addPlayer(request.getPlayerName());

		return new InitResponse().setId(game.getId());
	}
	
	@PostMapping("/start")
	public void startGame(@RequestBody StartRequest request) {
		log.info("startGame");
		
		Game game = getFromRequest(request); 
		game.start();
		
	}
	
	@PostMapping("/create")
	public InitResponse createGame(@RequestBody CreateRequest request) {
		log.info("createGame");
		
		Game game = gameFactory.create(request.getPlayerName());
		game.addPlayer(request.getPlayerName());
		games.add(request.getPlayerName(), game);
		
		return new InitResponse().setId(game.getId());
	}
	
	@PostMapping("/dismissMessage")
	public BaseResponse dismissMessage(@RequestBody DismissMessageRequest request) {
		log.info("dismissMessage");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	@PostMapping("/discardSidKetchum")
	public BaseResponse discardSidKetchum(@RequestBody DiscardSidKetchumRequest request) {
		log.info("discardSidKetchum");
		
		Game game = getFromRequest(request);
		return game.getPlayerById(request.getPlayerId()).doAction(request);
	}
	
	public Set<String> listGames() {
		return games.list();
	}
	
	private Game getFromRequest(BaseRequest request) {
		return games.getById(request.getGameId());
	}
	
	private Game getFromRequest(JoinRequest request) {
		return games.get(request.getGameName());
	}
}
