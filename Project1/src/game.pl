gameMode(pvp).
gameMode(pvb).
gameMode(bvb).

gameState(whiteToMove).
gameState(blackToMove).
gameState(whiteVictorious).
gameState(blackVictorious).
gameState(tie).
%gameState(stalemate).

%%% Game[Board, gameState, gameMode];

createPvPGame(Game):-
	initialBoard(Board),
	Game = [Board, whiteToMove, pvp], !.


getGameState(Game, GameState):-
	nth0(1,Game, GameState).

getGameMode(Game, GameMode):-
	nth0(2,Game, GameMode).

playGame(Game):-
	getBoard(Game, Board),
	repeat,
	clearConsole,
	printBoard(Board),
	getSourceCoords(SrcCol, SrcRow),
	convertToNumber(SrcCol, SrcColNumber),
	getPiece(Board, SrcColNumber, SrcRow, Piece),
	getGameState(Game, GameState),
	validateOwnership(Piece, GameState),
	getDestinyCoords(DestCol, DestRow),
	convertToNumber(DestCol, DestColNumber),
	validateMove(Piece, SrcColNumber, SrcRow, DestColNumber, DestRow, Board),
	makeMove(Board, SrcColNumber, SrcRow, DestColNumber, DestRow, NextBoard),
	updateGameState(Game, NextBoard, ContinueGame),
	playGame(ContinueGame).




	%Interaction functions

getInputCoords(SrcCol, SrcRow):-
	getColChar(SrcCol),
	getRowInt(SrcRow),
	get_code(_).


getSourceCoords(SrcCol,SrcRow):-
	write('Coords of Piece To Move: '), nl,
	getInputCoords(SrcCol, SrcRow), nl.

getDestinyCoords(SrcCol,SrcRow):-
	write('Coords of Piece New Position: '), nl,
	getInputCoords(SrcCol, SrcRow), nl.


	%Validation functions

validateOwnership(Piece, GameState):-
	GameState == whiteToMove,
	getPieceColor(Piece, Color),
	Color == 'White'.

validateOwnership(Piece, GameState):-
	GameState == blackToMove,
	getPieceColor(Piece, Color),
	Color == 'Black'.


validateOwnership(_, _):-
	%write('Invalid Piece!'), nl, %TODO: Ask that cut stuff to the teacher
	fail.

%TODO:
%%Piece Nao é preciso aqui
validateMove(Piece, SrcCol, SrcRow, DestCol, DestRow, Board):-
	differentPositions(SrcCol, SrcRow, DestCol, DestRow), !,
	differentColors(SrcCol, SrcRow, DestCol, DestRow, Board), !,
	getPieceName(Piece, PieceName),
	validBasicMove(PieceName, SrcCol, SrcRow, DestCol, DestRow), !,
	checkForJumping(PieceName, SrcCol, SrcRow, DestCol, DestRow, Board), !,
	makeMove(Board, SrcCol, SrcRow, DestCol, DestRow, TempBoard), !,
	printBoard(TempBoard), !,
	checkForCheck(TempBoard),
	write('Valid Move').


differentPositions(SrcCol, SrcRow, DestCol, DestRow):-
	SrcRow =\= DestRow ; SrcCol =\= DestCol.

differentPositions(_, _, _, _):-
	invalidMove.

differentColors(SrcCol, SrcRow, DestCol, DestRow, Board):-
	getPiece(Board, SrcCol, SrcRow, PieceSrc),
	getPiece(Board, DestCol, DestRow, PieceDest),
	getPieceColor(PieceSrc, ColorSrc),
	getPieceColor(PieceDest, ColorDest),
	ColorSrc \== ColorDest.

differentColors(_, _, _, _, _):-
	invalidMove.

invalidMove:-
	write('Invalid Move!'), nl,
	fail.


checkForJumping('Rook', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcCol == DestCol,
	DiffRows is (DestRow-SrcRow),
	DiffRows < 0, %Down
	findPieceOnCol(SrcCol, DestRow, SrcRow, Board).

checkForJumping('Rook', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcCol == DestCol,
	DiffRows is (DestRow-SrcRow),
	DiffRows > 0, %UP
	findPieceOnCol(SrcCol, SrcRow, DestRow, Board).

checkForJumping('Rook', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcRow == DestRow,
	DiffCols is (DestCol-SrcCol),
	DiffCols > 0, %Right
	findPieceOnRow(SrcRow, SrcCol, DestCol, Board).

checkForJumping('Rook', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcRow == DestRow,
	DiffCols is (DestCol-SrcCol),
	DiffCols < 0, %Left
	findPieceOnRow(SrcRow, DestCol, SrcCol, Board).

checkForJumping('Bishop', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows > 0, %UP
	DiffCols is (DestCol-SrcCol),
	DiffCols < 0, %Left

	LowRow is (SrcRow+1),
	HighCol is (SrcCol-1),
	findPieceOnDiagonalLeft(LowRow, HighCol, DestRow, DestCol, Board).

checkForJumping('Bishop', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows < 0, %Down
	DiffCols is (DestCol-SrcCol),
	DiffCols > 0, %Right

	LowRow is (DestRow+1),
	HighCol is (DestCol-1),
	findPieceOnDiagonalLeft(LowRow, HighCol, SrcRow, SrcCol, Board).

checkForJumping('Bishop', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows > 0, %UP
	DiffCols is (DestCol-SrcCol),
	DiffCols > 0, %Right

	LowRow is (SrcRow+1),
	LowCol is (SrcCol+1),
	findPieceOnDiagonalRight(LowRow, LowCol, DestRow, DestCol, Board).

checkForJumping('Bishop', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows < 0, %Down
	DiffCols is (DestCol-SrcCol),
	DiffCols < 0, %Left

	LowRow is (DestRow+1),
	LowCol is (DestCol+1),
	findPieceOnDiagonalRight(LowRow, LowCol, SrcRow, SrcCol, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcCol == DestCol,
	DiffRows is (DestRow-SrcRow),
	DiffRows < 0, %Down
	findPieceOnCol(SrcCol, DestRow, SrcRow, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcCol == DestCol,
	DiffRows is (DestRow-SrcRow),
	DiffRows > 0, %UP
	findPieceOnCol(SrcCol, SrcRow, DestRow, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcRow == DestRow,
	DiffCols is (DestCol-SrcCol),
	DiffCols > 0, %Right
	findPieceOnRow(SrcRow, SrcCol, DestCol, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	SrcRow == DestRow,
	DiffCols is (DestCol-SrcCol),
	DiffCols < 0, %Left
	findPieceOnRow(SrcRow, DestCol, SrcCol, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows > 0, %UP
	DiffCols is (DestCol-SrcCol),
	DiffCols < 0, %Left

	LowRow is (SrcRow+1),
	HighCol is (SrcCol-1),
	findPieceOnDiagonalLeft(LowRow, HighCol, DestRow, DestCol, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows < 0, %Down
	DiffCols is (DestCol-SrcCol),
	DiffCols > 0, %Right

	LowRow is (DestRow+1),
	HighCol is (DestCol-1),
	findPieceOnDiagonalLeft(LowRow, HighCol, SrcRow, SrcCol, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows > 0, %UP
	DiffCols is (DestCol-SrcCol),
	DiffCols > 0, %Right

	LowRow is (SrcRow+1),
	LowCol is (SrcCol+1),
	findPieceOnDiagonalRight(LowRow, LowCol, DestRow, DestCol, Board).

checkForJumping('Queen', SrcCol, SrcRow, DestCol, DestRow, Board):-
	DiffRows is (DestRow - SrcRow),
	DiffRows < 0, %Down
	DiffCols is (DestCol-SrcCol),
	DiffCols < 0, %Left

	LowRow is (DestRow+1),
	LowCol is (DestCol+1),
	findPieceOnDiagonalRight(LowRow, LowCol, SrcRow, SrcCol, Board).

checkForJumping('King', SrcCol, SrcRow, DestCol, DestRow, Board).
checkForJumping('Knight', SrcCol, SrcRow, DestCol, DestRow, Board).

checkForJumping(_, _, _, _, _, _):-
	invalidMove.

makeMove(Board, SrcCol, SrcRow, DestCol, DestRow, TempBoard):-
	getPiece(Board, SrcCol, SrcRow, Piece),
	nonePiece(NonePiece),
	write(SrcCol), write(SrcRow), nl,
	setPiece(Board, SrcCol, SrcRow, NonePiece, TempTempBoard),
	setPiece(TempTempBoard, DestCol, DestRow, Piece, TempBoard).

checkForCheck(TempBoard):-
	getPiece(TempBoard, WhiteKingCol, WhiteKingRow, 'King', 'White'),
	getPiece(TempBoard, BlackKingCol, BlackKingRow, 'King', 'Black'),
	\+(makePseudoMoves('Black', TempBoard, WhiteKingCol, WhiteKingRow)),
	\+(makePseudoMoves('White', TempBoard, BlackKingCol, BlackKingRow)).

makePseudoMoves('Black', TempBoard, DestCol, DestRow):-
	getPiece(TempBoard, Col, Row, PieceName, PieceColor),
	PieceColor == 'Black',
	validBasicMove(PieceName, Col, Row, DestCol, DestRow),%TODO: Doesn't show Invalid Move when fails cause it's pseudo
	checkForJumping(PieceName, Col, Row, DestCol, DestRow, TempBoard).


makePseudoMoves('White', TempBoard, DestCol, DestRow):-
	getPiece(TempBoard, Col, Row, PieceName, PieceColor),
	PieceColor == 'White',
	validBasicMove(PieceName, Col, Row, DestCol, DestRow),
	checkForJumping(PieceName, Col, Row, DestCol, DestRow, TempBoard).

updateGameState(Game, NextBoard, ContinueGame):-
	gameOver(Game, NextBoard, ContinueGame).

updateGameState(Game, NextBoard, ContinueGame):-
	changeTurn(Game, NextBoard, ContinueGame).

gameOver(Game, NextBoard, ContinueGame):-
	kingOnLastRow('White', NextBoard),
	\+(blackCanTie(NextBoard)),
	getGameMode(Game, GameMode),
	ContinueGame = [NextBoard, whiteVictorious, GameMode], !,
	write(white).

gameOver(Game, NextBoard, ContinueGame):-
	kingOnLastRow('Black', NextBoard),
	getGameMode(Game, GameMode),
	ContinueGame = [NextBoard, blackVictorious, GameMode], !,
	write(black).

gameOver(Game, NextBoard, ContinueGame):-
	kingOnLastRow('White', NextBoard),
	blackCanTie(NextBoard),
	getGameMode(Game, GameMode),
	ContinueGame = [NextBoard, tie, GameMode],!,
	write(tie).

kingOnLastRow(Color, Board):-
	getPiece(Board, _, 8, 'King', Color).

blackCanTie(Board):-
	getPiece(Board, Col, 7, 'King', 'Black'),
	differentColors(Col, 7, Col, 8, Board),
	makeMove(Board, Col, 7, Col, 8, NextBoard),
	checkForCheck(NextBoard).

blackCanTie(Board):-
	getPiece(Board, Col, 7, 'King', 'Black'),
	NextCol is Col+1,
	differentColors(Col, 7, NextCol, 8, Board),
	makeMove(Board, Col, 7, NextCol, 8, NextBoard),
	checkForCheck(NextBoard).

blackCanTie(Board):-
	getPiece(Board, Col, 7, 'King', 'Black'),
	LastCol is Col-1,
	differentColors(Col, 7, LastCol, 8, Board),
	makeMove(Board, Col, 7, LastCol, 8, NextBoard),
	checkForCheck(NextBoard).

changeTurn(Game, NextBoard, ContinueGame):-
	getGameMode(Game, GameMode),
	getGameState(Game, GameState),
	(
		GameState == whiteToMove ->
		NextGameState = blackToMove;
		NextGameState = whiteToMove
	),
	ContinueGame = [NextBoard, NextGameState, GameMode], !.
