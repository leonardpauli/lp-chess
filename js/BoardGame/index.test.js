// index.test.js

import {keyfix: kf, declarative, range} from 'lp-utils'

import BoardGame from './index'

describe('BoardGame', ()=> {
	it('extendable', ()=> {
		const TicTacToe = declarative(({Player})=> ({
			inherits: [BoardGame],
			players: ['O', 'X'].map(v=> ({inherits: [Player], name: v}))
			'on.start': [
				{}
			],
		}))
	})
})