package tick_tack_toe;

import tick_tack_toe.Constants.Symbol;

public class Match {
    private Player player1;
    private Player player2;
    private Player nextMove; // if true > player 1 else player2
    private Symbol player1Symbol;
    private Symbol player2Symbol;
    private Board board;

    public Match(Player player1, Player player2, boolean player1FirstMove) {
        this.player1 = player1;
        this.player1 = player1;
        this.nextMove = player1FirstMove ? player1 : player2;
        this.board = new Board();
    }

    public boolean makeMove(Player player) throws Exception {
        if (this.nextMove != player) {
            throw new Exception("Wrong player made the move");
        }

        return true;

    }

}
