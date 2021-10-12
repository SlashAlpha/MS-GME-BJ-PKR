package slash.code.game.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slash.code.game.config.JmsConfig;
import slash.code.game.model.Play;
import slash.code.game.model.PlayRepository;
import slash.code.game.model.Player;
import slash.code.game.service.PlayService;
import slash.code.game.service.PlayerService;
import slash.code.game.viewModel.Mapper;
import slash.code.game.viewModel.PlayerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RequestMapping("api/v1/poker/players")
@RestController
public class PlayerController {
    PlayerService playerService;
    Mapper mapper;
    PlayRepository playRepository;
    PlayService playService;

    public PlayerController(PlayerService playerService, Mapper mapper, PlayRepository playRepository, PlayService playService) {
        this.playerService = playerService;
        this.mapper = mapper;
        this.playRepository = playRepository;
        this.playService = playService;
    }

    @PostMapping("/newplayer{playId}")
    public ResponseEntity<UUID> createPlayer(@PathVariable String playId, @RequestBody PlayerDTO playerDTO){


        Player player=mapper.convertNewPlayerDTOToPlayer(playerDTO);
        playService.addPLayerToPlay(UUID.fromString(playId),player.getId());

        playService.addPLayerToPlay(UUID.fromString( playId),  playerService.newPlayer("Pedro",32,6000).getId());
        playService.addPLayerToPlay(UUID.fromString( playId),  playerService.newPlayer("Tabatha",34,8000).getId());
        playService.addPLayerToPlay(UUID.fromString( playId),  playerService.newPlayer("Manuel",37,5000).getId());
        playService.addPLayerToPlay(UUID.fromString( playId),  playerService.newPlayer("Samantha",31,4000).getId());



        if(!player.equals(null)){ System.out.println("player saved");
    return  new ResponseEntity<UUID>(player.getId(), HttpStatus.OK);}


    else{return  null;}
    }

    @GetMapping("/{playId}")
        ResponseEntity<List<UUID>> getPlayers(@PathVariable String playId){
        List<UUID> playersId=new ArrayList<>();
        List<Player>players=playService.getPlayPlayers(UUID.fromString(playId));

        for (Player player:players){
            playerService.sendIdToDealer(player.getId());
        playersId.add(player.getId());
        }









        return new ResponseEntity<List<UUID>>(playersId,HttpStatus.OK);
    }
    
    

    @GetMapping("/blinds{playId}")
    public void setBlinds(@PathVariable UUID playId){
       Play play= playService.getPLay(playId);
        playerService.setBlind(play.getPlayers(), playService.getBlind());

    }


    @PostMapping("/bet")
    public void playerBetCallRaise(@RequestBody PlayerDTO playerDTO){
        Player player=mapper.convertToPlayer(playerDTO);
    }




}