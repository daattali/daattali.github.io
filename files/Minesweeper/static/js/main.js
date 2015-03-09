main = function() {
	var SAFE_CELL = 0;
	var MINE_CELL = 1;

    var gridSize = 0;
	var numMines = 0;
	var cell = [];
	var gameInProgress = false;

	return {
		cellClicked : function(e) {
			if (!gameInProgress) return;

			// ignore all clicks on a clicked cell
			var el = $(e.target);
			if (el.hasClass('clicked')) return;

			// right click
			// for convenience, let the user mark cells as mines or maybe mines
			if (e.button > 0) {
				if (el.hasClass('flag')) {
					el.removeClass('flag').addClass('maybe');
					el.text('?')
				} else if (el.hasClass('maybe')) {
					el.removeClass('maybe');
					el.text('')
				} else {
					el.addClass('flag');
					el.text('*')
				}
				return;
			}

			// left click
			el.addClass('clicked').removeClass('flag').removeClass('maybe');
			el.text('');

			var cellNum = parseInt(el.attr('data-cell-id'));
			if ( cell[cellNum] == MINE_CELL) {
				// you lose!
				main.revealMines();
				alert('Ouch, you hit a mine! You lose :(');
				gameInProgress = false;
			} else {
				main.revealCell(cellNum);
			}

			// if num mines == num unclicked cells, user won!
			var allCells = $('#board .cell');
			var clickedCells = $('#board .cell.clicked');
			numCellsLeft = allCells.length - clickedCells.length;

			if (numCellsLeft == numMines) {
				alert('Congrats, you won!');
				gameInProgress = false;
			}
		},

		// show the contents of a cell
		revealCell : function(num) {
			main.getCell(num).addClass('clicked');
			var adjCells = main.getAdjCells(num);

			var numAdjMines = 0;
			for (i in adjCells){
				if (cell[adjCells[i]] == MINE_CELL) {
					numAdjMines++;
				}
			}

			// if there are no mines nearby, show the adjacent cells
			if (numAdjMines == 0) {
				for (i in adjCells){
					main.revealCell(adjCells[i], num);
				}
			}
			// if there are mines around, show how many
			else {
				main.getCell(num).text(numAdjMines);
			}
		},

		// get all the adjacent cells to the given cell
		getAdjCells : function(num) {
			var adjCells = [];
			var addToAdjCells = function(num) {
				// takes care of not adding cells that are outside the board
				// or cells that have been seen already
				if (num >= 0 && num < gridSize*gridSize &&
				  !main.getCell(num).hasClass('clicked')) {
					adjCells.push(num);
				}
			};

			// make sure to not add cells on the other side of the board
			if (num % gridSize != 0) {
				addToAdjCells(num - gridSize - 1);
				addToAdjCells(num - 1);
				addToAdjCells(num + gridSize - 1);
			}
			if ((num+1) % gridSize != 0) {
				addToAdjCells(num - gridSize + 1);
				addToAdjCells(num + 1);
				addToAdjCells(num + gridSize + 1);
			}
			addToAdjCells(num - gridSize);
			addToAdjCells(num + gridSize);

			return adjCells;
		},

		// 
		inBoard : function(num) {
			return (num >= 0 && num < gridSize*gridSize);
		},

		// show all mines (either cuz you lose or from cheating)
		revealMines : function() {
			var i = 0;
			for (i = 0; i < cell.length; i++) {
				if (cell[i] == MINE_CELL) {
					main.getCell(i).addClass('mine');
				}
			}
		},

		// get the HTML element of a cell based on id
		getCell : function(num) {
			return $('#board .cell[data-cell-id=' + num + ']');
		},

		newGame : function() {
			// reset variables
			gameInProgress = true;
			numMines = parseInt($('#num-mines').val());
			gridSize = parseInt($('#grid-size').val());
			cell = [];

			// make board game with first X cells as mines
			var i = 0;
			for (i = 0; i < gridSize*gridSize; i++) {
				if (i < numMines) {
					cell.push(MINE_CELL);
				} else {
					cell.push(SAFE_CELL);
				}
			}
			var randomSort = function(a,b) {
				return( parseInt( Math.random()*10 ) %2 );
			}
			// randomize the cells so that mines are distributed better
			// NOTE this is a very bad randomizer! 
			cell.sort(randomSort);

			// draw the board
			var boardHtml = '';
			for (i = 0; i < gridSize*gridSize; i++) {
				var newRow = (i % gridSize == 0);
				var firstRow = (i == 0);
				var lastRow = (i == gridSize*gridSize);
				if (newRow && !firstRow) {
					boardHtml += '</div>';
				}
				if (newRow && !lastRow) {
					boardHtml += '<div class="row">';
				}
				boardHtml += '<div data-cell-id="' + i + '" class="cell"></div>';

			}
			$('#board').html(boardHtml);
		},

		init : function() {
			main.newGame();
			$(document).on("mouseup", ".cell", main.cellClicked);
			$(document).on("click", "#newgame-btn", main.newGame);
			$(document).on("click", "#cheat-btn", main.revealMines);
			
			// disable context menu on right click
			window.addEventListener('contextmenu', function(evt){ evt.preventDefault(); });
		}
	};
}();

$(document).ready(function(){ main.init(); });