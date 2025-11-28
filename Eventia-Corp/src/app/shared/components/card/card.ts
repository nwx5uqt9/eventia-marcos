import { ChangeDetectionStrategy, Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card',
  standalone:true,
  imports: [CommonModule],
  templateUrl: './card.html',
  styleUrl: './card.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Card {
  @Input() title: string = 'Título de la Card';
  @Input() description: string = 'Descripción de ejemplo de la card.';
  @Input() imageUrl: string = '';
  @Input() buttonText: string = 'Más información';
  @Input() buttonClass: string = 'btn-primary';
  @Input() precio: number | null | undefined = null;
  @Input() ubicacion: string | null | undefined = null;
  @Output() onButtonClick = new EventEmitter<void>();

  handleClick(event: Event): void {
    event.preventDefault();
    this.onButtonClick.emit();
  }
}
