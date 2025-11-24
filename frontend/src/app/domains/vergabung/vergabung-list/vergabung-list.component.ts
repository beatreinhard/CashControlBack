import {Component, OnInit} from '@angular/core';
import {VergabungControllerApi} from '../../../generated';
import {JsonPipe} from '@angular/common';

@Component({
  selector: 'app-vergabung-list',
  imports: [
    JsonPipe
  ],
  templateUrl: './vergabung-list.component.html',
  styleUrl: './vergabung-list.component.css'
})
export class VergabungListComponent implements OnInit{
  vergabungData: any;

  constructor(private vergabungController: VergabungControllerApi) {
  }

  ngOnInit() {
    this.vergabungController.getAllVergabungen().subscribe({
      next: (res) => {
        console.log('API response Vergabung:', res);
        this.vergabungData = res;
      },
      error: (err) => console.error('API error:', err),
    });
  }
}
