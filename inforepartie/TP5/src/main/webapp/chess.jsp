<%@ page import="com.github.bhlangonijr.chesslib.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private final Board board = new Board();

    public String getCellColor(int rankIndex, int fileIndex) {
        return ((rankIndex + fileIndex) % 2 == 0) ? "blanc" : "noir";
    }

    public String getPieceType(Piece piece) {
        PieceType type = piece.getPieceType();
        switch (type) {
            case PAWN:
                return "pion";
            case ROOK:
                return "tour";
            case KNIGHT:
                return "cavalier";
            case BISHOP:
                return "fou";
            case QUEEN:
                return "dame";
            case KING:
                return "roi";
            default:
                return "";
        }
    }

    public String getPieceColor(Piece piece) {
        return Side.WHITE.equals(piece.getPieceSide()) ? "blanc" : "noir";
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Chess Game</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="plateau">
<%
    for (int rankIndex = 7; rankIndex >= 0; rankIndex--) {
        Rank rank = Rank.values()[rankIndex];
        for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
            File file = File.values()[fileIndex];
            Square square = Square.encode(rank, file);
            Piece piece = board.getPiece(square);
            String cellColor = getCellColor(rankIndex, fileIndex);
%>
    <p class="<%= cellColor %>" style="position: relative;">
        <%
            if (!Piece.NONE.equals(piece)) {
                String typeClass = getPieceType(piece);
                String colorClass = getPieceColor(piece);
        %>
        <i class="<%= typeClass %> <%= colorClass %>"></i>
        <%
            }
        %>
    </p>
<%
        }
    }
%>
</div>
</body>
</html>
