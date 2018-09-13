// BoardGame
// Created by Leonard Pauli, sept 2018

import {keyfix: kf, declarative} from 'lp-utils'

const Time = Number // TODO: fix proper with helpers, type conversions, etc


const BoardGame = declarative(({
	Player, Board, State, Piece, Round,
})=> ({
	players: kf({'type{many}': Player}),
	board: kf({'type': Board}),

	states: kf({'type{many}': State}),
	rounds: kf({'type{many}': Round}),


	Player: {
		name: {type: String},
		moves: kf({'type{many}': Move}),
		pieces: kf({'type{many}': Piece}),
		active: {type: Boolean},
	},

	Board: declarative(({  })=> ({
		/*tiles is many Tile:
			edges is many Edge:
				tiles is many{2} Tile
				movement.type{is Movement.Type or Empty} with origin{is in tiles}:
					' returns:
						- None if blocked/one directional
						- same if unidirectional
						- else conditional // (eg. pawn in chess cannot move backwards)
			pieces is many Piece
			position is Position:
				as String
				*/
	})),

	State: {},

	Piece: declarative(({  })=> ({
		/*
		owner.player is Player
		tile is Tile
		type is Type:
			name is String
			moves.possible.types is many Movement.Type
			// moves.possible.types with state: ...
		*/
	})),

	Round: declarative(({  })=> ({
		/*
		moves is many Move:
			time{(start, end) is Date, duration is Time}
			player is Player
			piece is Piece
			movement is Movement:
				type is Movement.Type
					name is String
					reaches{is Boolean} with {tile, startingTile}
				from is Position or Empty
				to is Position or Empty
				do reverse: ... // for undo
		*/
	})),

}))

export default BoardGame
