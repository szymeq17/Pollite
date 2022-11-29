import { Component, OnInit } from '@angular/core';
import {Poll} from "../../../model/Poll";

@Component({
  selector: 'app-polls',
  templateUrl: './polls.component.html',
  styleUrls: ['./polls.component.scss']
})
export class PollsComponent implements OnInit {

  polls: Poll[] = [
    {
      id: 1,
      ownerUsername: 'szymeq17',
      text: 'Kto powinien wygrać wybory?',
      pollAnswers: [
        {
          id: 1,
          text: 'PiS'
        },
        {
          id: 2,
          text: 'Polska 2050'
        },
        {
          id: 3,
          text: 'PO'
        },
        {
          id: 4,
          text: 'Konfederacja'
        }
      ],
      startDateTime: new Date(2022, 11, 27, 0, 0, 0, 0)
    },
    {
      id: 2,
      ownerUsername: 'szymeq17',
      text: 'Które zwierze lubisz najbardziej?',
      pollAnswers: [
        {
          id: 5,
          text: 'Pies'
        },
        {
          id: 6,
          text: 'Kot'
        },
        {
          id: 7,
          text: 'Chomik'
        },
        {
          id: 8,
          text: 'Świnka morska'
        }
      ]
    },
    {
      id: 2,
      ownerUsername: 'szymeq17',
      text: 'Które zwierze lubisz najbardziej?',
      pollAnswers: [
        {
          id: 5,
          text: 'Pies'
        },
        {
          id: 6,
          text: 'Kot'
        },
        {
          id: 7,
          text: 'Chomik'
        },
        {
          id: 8,
          text: 'Świnka morska'
        }
      ]
    },
    {
      id: 2,
      ownerUsername: 'szymeq17',
      text: 'Które zwierze lubisz najbardziej?',
      pollAnswers: [
        {
          id: 5,
          text: 'Pies'
        },
        {
          id: 6,
          text: 'Kot'
        },
        {
          id: 7,
          text: 'Chomik'
        },
        {
          id: 8,
          text: 'Świnka morska'
        }
      ]
    }
  ];

  constructor() { }

  ngOnInit(): void {
  }

}
