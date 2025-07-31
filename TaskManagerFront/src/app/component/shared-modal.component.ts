import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-shared-modal',
  templateUrl: './shared-modal.component.html',
  styleUrls: ['./shared-modal.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class SharedModalComponent {
  @Input() open = false;
  @Output() onClose = new EventEmitter<void>();

  close() {
    this.onClose.emit();
  }
} 