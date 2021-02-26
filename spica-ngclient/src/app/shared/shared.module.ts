import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { HeaderComponent } from './components/header/header.component';


import { FooterComponent } from './components/footer/footer.component';
import { AreaComponent } from './widgets/area/area.component';
import { HighchartsChartModule } from 'highcharts-angular';
import { CardComponent } from './widgets/card/card.component';
import { PieComponent } from './widgets/pie/pie.component';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    AreaComponent,
    CardComponent,
    PieComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    HighchartsChartModule
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    AreaComponent,
    CardComponent,
    PieComponent
  ]
})
export class SharedModule { }
