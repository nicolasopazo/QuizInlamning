package com.nico;

public class Game extends Thread {

    Player currentPlayer;
    Player player1;
    Player player2;
    boolean player1Answered = true;
    boolean player2Answered = true;
    String[] answers = {"", ""};
    Thread thread;
    int questionNr = -1;
    String currentQuestion;
    String correctAnswer;
    String[] currentOptions;

    Questions q = new Questions();

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
        thread = new Thread(this);
    }

    public void run() {
        String answer;
        sendWaitingToOtherPlayer();
        while (true) {
            if (player1Answered && player2Answered) {
                questionNr ++;
                if(questionNr>3) {
                    sendResultsToPlayers();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendExitToBothPlayers();

                    //om man vill att servern ska avslutas efter att två spelare har spelat klart
//                    exit();
                }
                correctAnswer = q.correctAnswers[questionNr];
                currentPlayer = player1;
                if (questionNr != 0) {
                    sendWaitingToOtherPlayer();
                    sendResultsToPlayers();
                }
                player1Answered = false;
                player2Answered = false;
            } else {
                currentPlayer = player2;
            }
            currentQuestion = q.questions[questionNr];
            currentOptions = q.options[questionNr];
            sendWaitingToOtherPlayer();
            currentPlayer.sendMessageToClient(currentQuestion);
            currentPlayer.sendMessageToClient(currentOptions);
            currentPlayer.sendMessageToClient("Correct " + correctAnswer);
            answer = currentPlayer.getAnswer();
            answers[currentPlayer.getPlayerNr()] = answer;
            setPlayerAnswered();
            changePlayer();
        }
    }

    public void exit() {
        try {
            Thread.sleep(6000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changePlayer() {
        if (currentPlayer == player1) {
            currentPlayer= player2;
        } else {
            currentPlayer=player1;
        }
    }

    public void setPlayerAnswered() {
        if (currentPlayer == player1) {
            player1Answered = true;
        } else {
            player2Answered = true;
        }
    }

    public void sendExitToBothPlayers() {
        player1.sendMessageToClient("EXIT");
        player2.sendMessageToClient("EXIT");

    }

    private void sendResultsToPlayers() {

        if(answers[0].equals(answers[1])) {
            player1.sendMessageToClient("Result LIKA!");
            player2.sendMessageToClient("Result LIKA!");
        } else if(answers[0].equals("correct")) {
            player1.sendMessageToClient("Result DU VANN!");
            player2.sendMessageToClient("Result DU FÖRLORADE!");
        } else {
            player2.sendMessageToClient("Result DU VANN!");
            player1.sendMessageToClient("Result DU FÖRLORADE!");
        }
    }


    public void sendWaitingToOtherPlayer() {
        if (currentPlayer == player1) {
            player2.sendMessageToClient("Väntar på den andra spelaren.");
        } else {
            player1.sendMessageToClient("Väntar på den andra spelaren.");
        }
    }
}
